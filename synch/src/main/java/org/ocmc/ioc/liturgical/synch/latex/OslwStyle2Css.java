package org.ocmc.ioc.liturgical.synch.latex;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.ocmc.ioc.liturgical.utils.FileUtils;

public class OslwStyle2Css {
	
	/**
	 * 
	 * \DeclareDocumentCommand\ltDialog{m m g g g g g g}{%
     * \ltSidsFormat{\ltTextBlack}{\ltTextBlack}{\ltTextBlack}{\ltTextBlack}%
	 */
	public void process(String pathIn, String pathOut) {
		Queue<String> q = new LinkedList<>();
		String cmdLine = "DeclareDocumentCommand";
		String cmdLineAlt = "newcommand";
		String formatLine = "ltSidsFormat{";
		List<String> commands = new ArrayList<String>();
		
		for (String line : FileUtils.linesFromFile(new File(pathIn))) {
			if (line.contains("ltRubricDialog}")) {
				System.out.print("");
			}
			if (line.contains(cmdLine) || line.contains(cmdLineAlt)) {
				if (! q.isEmpty()) {
					q.clear();
				}
				q.add(line);
			} else if (line.contains(formatLine)) {
				String command = "";
				if (q.isEmpty()) {
				} else {
					String cmd = q.remove();
					if (cmd.contains(cmdLine)) {
						cmd = cmd.replace("\\"+cmdLine + "\\lt", "{");
						cmd = cmd.replace("}{", "{");
						String parts[] = cmd.split("\\{");
						command = parts[1];
//						String args = parts[2].trim();
//						String cnt[] = args.split(" ");
					} else if (cmd.contains(cmdLineAlt)) {
						cmd = cmd.replace("\\"+cmdLineAlt + "{\\lt", "{");
						cmd = cmd.replace("}[", "{");
						String parts[] = cmd.split("\\{");
						command = parts[1];
					}
					line = line.replace("SidsFormat", "}");
					line = line.replace("}%", "}{");
					line = line.replaceAll("\\\\lt", "");
					String fmtParts [] = line.split("\\}\\{");
					int fmtCount = fmtParts.length-1;
					MetaCommand metaCmd = new MetaCommand();
					metaCmd.setCmdName(command);
					metaCmd.setArgCount(fmtCount);
					
					for (String s : fmtParts) {
						if (s.trim().length() > 0) {
							metaCmd.addArgFormat(s.trim());
						}
					}
//					System.out.println(metaCmd.toJsonString());
					StringBuffer sb = new StringBuffer();
					sb.append("	, ");
					sb.append(metaCmd.cmdName.toUpperCase());
					sb.append("(\"");
					sb.append(metaCmd.cmdName);
					sb.append("\", \"");
					sb.append(metaCmd.cmdName);
					sb.append("\")");
					commands.add(sb.toString());
				}
			}
		}
		Collections.sort(commands);
		for (String command : commands) {
			System.out.println(command);
		}
	}

	public static void main(String[] args) {
		String pathIn = "/Users/mac002/git/ocmc/service.book.ke.oak/priestsservicebook/system/ocmc-liturgical-text.sty";
		String pathOut = "";
		OslwStyle2Css it = new OslwStyle2Css();
		it.process(pathIn, pathOut);
	}

}
