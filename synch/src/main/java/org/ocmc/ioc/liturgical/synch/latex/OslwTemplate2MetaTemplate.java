package org.ocmc.ioc.liturgical.synch.latex;

import java.io.File;
import java.util.Stack;

import org.ocmc.ioc.liturgical.schemas.constants.TEMPLATE_NODE_TYPES;
import org.ocmc.ioc.liturgical.schemas.models.db.docs.templates.TemplateNode;
import org.ocmc.ioc.liturgical.utils.FileUtils;

/**
 * Reads recursively all the LaTeX OSLW template files in specified directory
 * and converts writes them to meta-template format.  This format can
 * be read by the OLW template editor.
 * 
 * @author mac002
 *
 */
public class OslwTemplate2MetaTemplate {
	
	public void process(String pathIn, String pathOut) {
		
		for (File f : FileUtils.getFilesInDirectory(pathIn, "tex")) {
			System.out.println(f.getPath());
			
			TemplateNode parentNode = new TemplateNode();
			TemplateNode childNode = new TemplateNode();
			parentNode.setTitle(TEMPLATE_NODE_TYPES.TEMPLATE);
			parentNode.setSubtitle(f.getName());
			
			Stack<TemplateNode> q = new Stack<TemplateNode>();

			for (String line : FileUtils.linesFromFile(f)) {
				line = line.trim();
				if (line.length() > 0 && line.startsWith("\\")) {
					line = line.replaceAll("\\}", "");
					String [] parts = line.split("\\{");
					String cmd = "";
					if (parts[0].startsWith("\\lt")) {
						cmd = parts[0].substring(3);
						childNode = new TemplateNode();
						childNode.setTitle(TEMPLATE_NODE_TYPES.typeForName(cmd));
						if (childNode.title != null) {
							switch (cmd) {
							case ("Chapter" ):
							case ("Section"):
							case ("SubSection"):
							case ("SubSubSection"): { 
								childNode.setSubtitle(parts[1] + "~" + parts[2]);
								boolean childIsSubdivision = false;
								childIsSubdivision = TEMPLATE_NODE_TYPES.childIsSubDivision(
										parentNode.title
										, childNode.title);
								if (childIsSubdivision) {
									q.push(parentNode);
									parentNode = childNode;
								} else {
									if (q.isEmpty()) {
										// ignore
									} else {
										do {
											TemplateNode popped = q.pop();
											popped.appendNode(parentNode);
											parentNode = popped;
											childIsSubdivision = TEMPLATE_NODE_TYPES.childIsSubDivision(
													parentNode.title
													, childNode.title);
										} while (! q.isEmpty() && ! childIsSubdivision);
										q.push(parentNode);
										parentNode = childNode;
									}
								}
								break;
							}
							default: {
								for (int i = 1; i < parts.length; i = i+2) {
									TemplateNode sidNode = new TemplateNode();
									sidNode.setTitle(TEMPLATE_NODE_TYPES.SID);
									String topic = parts[i];
									String key = parts[i+1];
									sidNode.setSubtitle(topic + "~" + key);
									childNode.appendNode(sidNode);
								}
								parentNode.appendNode(childNode);
							}
						}
						} else {
							System.out.println("Unknown command: " + cmd);
						}
					} else if (line.startsWith("\\input")){ // e.g. \input{templates/tmp.typika}
						line = line.replaceAll("\\}", "");
						parts = line.split("/");
						String templateName = parts[parts.length-1].replace("tmp.", "");
						childNode = new TemplateNode();
						childNode.setTitle(TEMPLATE_NODE_TYPES.INSERT_TEMPLATE);
						childNode.setSubtitle("templates~" + templateName);
						/**
						 * TODO:
						 * 1. Read the template to determine what it's top most node is.
						 * 2. pop the stack until the top node is a child.
						 */
						parentNode.appendNode(childNode);
					} else {
						cmd = parts[0].substring(1);
					}
				}
			}
			TemplateNode finalNode = null;
			childNode = parentNode;
			while (! q.isEmpty()) {
				finalNode = q.pop();
				finalNode.appendNode(childNode);
				childNode = finalNode;
			}
			parentNode.setPrettyPrint(true);
//			System.out.println(finalNode.toJsonString());
			FileUtils.writeFile(pathOut + f.getName(), finalNode.toJsonString());
		}
	}

	private TEMPLATE_NODE_TYPES getInsertTopNode(String filename) {
		TEMPLATE_NODE_TYPES result = null;
		return result;
	}
	public static void main(String[] args) {
		String pathIn = "/Users/mac002/git/ocmc/service.book.ke.oak/priestsservicebook/templates";
//		String pathIn = "/Users/mac002/git/ocmc/service.book.ke.oak/priestsservicebook/templates/test";
		String pathOut = "/Users/mac002/canBeRemoved/latex2olw/";
		OslwTemplate2MetaTemplate n = new OslwTemplate2MetaTemplate();
		n.process(pathIn, pathOut);
	}

}
