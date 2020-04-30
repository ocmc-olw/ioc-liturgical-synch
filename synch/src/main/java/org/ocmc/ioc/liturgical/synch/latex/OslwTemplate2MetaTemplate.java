package org.ocmc.ioc.liturgical.synch.latex;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

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
	
	List<File> files = new ArrayList<File>();
	Map<String, TEMPLATE_NODE_TYPES> fileMap = new TreeMap<String,TEMPLATE_NODE_TYPES>();
	
	public void process(String pathIn, String pathOut) {
		
		this.createFileMap(pathIn);
		
		for (File f : FileUtils.getFilesInDirectory(pathIn, "tex")) {
			System.out.println(f.getPath());
			TemplateNode parentNode = new TemplateNode();
			TemplateNode childNode = new TemplateNode();
			parentNode.setTitle(TEMPLATE_NODE_TYPES.TEMPLATE);
			parentNode.setSubtitle(this.normalizeTemplateName(f.getName()));
			
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
									switch (childNode.children.size()) {
									case (0): {
										sidNode.setFormat(childNode.title.fmt0);
										break;
										}
									case (1): {
										sidNode.setFormat(childNode.title.fmt1);
										break;
										}
									case (2): {
										sidNode.setFormat(childNode.title.fmt2);
										break;
										}
									case (3): {
										sidNode.setFormat(childNode.title.fmt3);
										break;
										}
									}
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
						if (templateName.endsWith(".tex")) {
							templateName = templateName.substring(0, templateName.length()-4);
						}
						childNode = new TemplateNode();
						childNode.setTitle(TEMPLATE_NODE_TYPES.INSERT_TEMPLATE);
						childNode.setSubtitle("templates~" + templateName);
						TEMPLATE_NODE_TYPES insertTopNode = this.fileMap.get(templateName);
						boolean childIsSubdivision = false;
						childIsSubdivision = TEMPLATE_NODE_TYPES.childIsSubDivision(
								parentNode.title
								, insertTopNode);
						while (! childIsSubdivision && ! q.empty()){
							TemplateNode popped = q.pop();
							try {
								popped.appendNode(parentNode);
								parentNode = popped;
								childIsSubdivision = TEMPLATE_NODE_TYPES.childIsSubDivision(
										parentNode.title
										, insertTopNode);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
//						parentNode.appendNode(childNode);
						q.push(parentNode);
						parentNode = childNode;
					} else {
						cmd = parts[0].substring(1);
					}
				} else {
					if (line.startsWith("%name")) {
						String [] metaParts = line.split(":");
						if (metaParts.length == 2 && parentNode.title == TEMPLATE_NODE_TYPES.TEMPLATE) {
							parentNode.setName(metaParts[1].trim());
						}
					} else if (line.startsWith("%desc")) {
						String [] metaParts = line.split(":");
						if (metaParts.length == 2 && parentNode.title == TEMPLATE_NODE_TYPES.TEMPLATE) {
							parentNode.setDesc(metaParts[1].trim());
						}
					} else if (line.startsWith("%juri")) {
						String [] metaParts = line.split(":");
						if (metaParts.length == 2 && parentNode.title == TEMPLATE_NODE_TYPES.TEMPLATE) {
							parentNode.setJurisdiction(metaParts[1].trim());
						}
					}
				}
			}
			TemplateNode finalNode = null;
			if (q.isEmpty()) {
				finalNode = parentNode;
			} else {
				childNode = parentNode;
				while (! q.isEmpty()) {
					finalNode = q.pop();
					finalNode.appendNode(childNode);
					childNode = finalNode;
				}
			}
			finalNode.setPrettyPrint(true);
			try {
				String fileOut = f.getName().replace(".tex", ".json");
				FileUtils.writeFile(pathOut + fileOut, finalNode.toJsonString());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String normalizeTemplateName(String name) {
		String result = name.trim();
		try {
			if (result.startsWith("tmp")) {
				result = result.substring(4);
			}
			if (result.endsWith("tex")) {
				result = result.substring(0, result.length()-4);
			}
		} catch (Exception e) {
			e.printStackTrace();;
		}
		return result;
	}
	private void createFileMap(String pathIn) {
		for (File f : FileUtils.getFilesInDirectory(pathIn, "tex")) {
			this.files.add(f);
			this.mapTopNode(f);
		}
		
	}
	private void mapTopNode(File f) {
		TEMPLATE_NODE_TYPES result = null;
		for (String line : FileUtils.linesFromFile(f)) {
			line = line.replaceAll("\\}", "");
			String [] parts = line.split("\\{");
			String cmd = "";
			if (parts[0].startsWith("\\lt")) {
				cmd = parts[0].substring(3);
				result = TEMPLATE_NODE_TYPES.typeForName(cmd);
				String templateName = f.getName().replace("tmp.", "").replace(".tex", "");
				this.fileMap.put(templateName, result);
				System.out.println(templateName + ": " +  result.keyname);
				break;
			}
		}
	}
	public static void main(String[] args) {
		String pathIn = "/Users/mac002/git/ocmc/service.book.ke.oak/priestsservicebook/templates";
//		String pathIn = "/Users/mac002/git/ocmc/service.book.ke.oak/priestsservicebook/templates/test";
		String pathOut = "/Users/mac002/canBeRemoved/latex2olw/meta/";
		OslwTemplate2MetaTemplate n = new OslwTemplate2MetaTemplate();
		n.process(pathIn, pathOut);
	}

}
