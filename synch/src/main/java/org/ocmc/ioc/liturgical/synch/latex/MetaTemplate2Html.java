package org.ocmc.ioc.liturgical.synch.latex;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.ocmc.ioc.liturgical.schemas.models.db.docs.templates.TemplateNode;
import org.ocmc.ioc.liturgical.utils.FileUtils;

import com.google.gson.Gson;

public class MetaTemplate2Html {
	Gson gson = new Gson();
	Map<String,TemplateNode> nodeMap = new TreeMap<String,TemplateNode>();
	Map<String,String> htmlMap = new TreeMap<String,String>();
	
	public void process  (String pathIn, String pathOut) {
		/**
		 * Load all the meta-templates into a map because some
		 * templates include other templates.  So we need to load
		 * all of them up-front so we can find templates to insert
		 * when necessary.
		 */
		for (File f : FileUtils.getFilesInDirectory(pathIn, "json")) {
			TemplateNode node = gson.fromJson(FileUtils.getFileContents(f), TemplateNode.class);
			this.nodeMap.put(node.subtitle, node);
		}
		for (TemplateNode node : this.nodeMap.values()) {
			System.out.println(node.toJsonString());
			this.htmlMap.put(node.subtitle, this.toHtml(node));
		}
		for (String key : this.htmlMap.keySet()) {
			FileUtils.writeFile(pathOut + key + ".html", this.htmlMap.get(key));
		}
	}
	
	public String toHtml(TemplateNode templateNode) {
		StringBuffer sb = new StringBuffer();
		StringBuffer childrenContent = new StringBuffer();
		switch (templateNode.title) {
		case APR:
			break;
		case AUG:
			break;
		case CHAPTER:
			break;
		case DAY_01:
			break;
		case DAY_02:
			break;
		case DAY_03:
			break;
		case DAY_04:
			break;
		case DAY_05:
			break;
		case DAY_06:
			break;
		case DAY_07:
			break;
		case DAY_08:
			break;
		case DAY_09:
			break;
		case DAY_10:
			break;
		case DAY_11:
			break;
		case DAY_12:
			break;
		case DAY_13:
			break;
		case DAY_14:
			break;
		case DAY_15:
			break;
		case DAY_16:
			break;
		case DAY_17:
			break;
		case DAY_18:
			break;
		case DAY_19:
			break;
		case DAY_20:
			break;
		case DAY_21:
			break;
		case DAY_22:
			break;
		case DAY_23:
			break;
		case DAY_24:
			break;
		case DAY_25:
			break;
		case DAY_26:
			break;
		case DAY_27:
			break;
		case DAY_28:
			break;
		case DAY_29:
			break;
		case DAY_30:
			break;
		case DAY_31:
			break;
		case DAY_32:
			break;
		case DAY_33:
			break;
		case DAY_34:
			break;
		case DAY_35:
			break;
		case DAY_36:
			break;
		case DAY_37:
			break;
		case DAY_38:
			break;
		case DAY_39:
			break;
		case DAY_40:
			break;
		case DAY_41:
			break;
		case DAY_42:
			break;
		case DAY_43:
			break;
		case DAY_44:
			break;
		case DAY_45:
			break;
		case DAY_46:
			break;
		case DAY_47:
			break;
		case DAY_48:
			break;
		case DAY_49:
			break;
		case DAY_50:
			break;
		case DAY_51:
			break;
		case DAY_52:
			break;
		case DAY_53:
			break;
		case DAY_54:
			break;
		case DAY_55:
			break;
		case DAY_56:
			break;
		case DAY_57:
			break;
		case DAY_58:
			break;
		case DAY_59:
			break;
		case DAY_60:
			break;
		case DAY_61:
			break;
		case DAY_62:
			break;
		case DAY_63:
			break;
		case DAY_64:
			break;
		case DAY_65:
			break;
		case DAY_66:
			break;
		case DAY_67:
			break;
		case DAY_68:
			break;
		case DAY_69:
			break;
		case DAY_70:
			break;
		case DEC:
			break;
		case FEB:
			break;
		case FRIDAY:
			break;
		case INSERT_SECTION:
			break;
		case INSERT_TEMPLATE:
			break;
		case JAN:
			break;
		case JUL:
			break;
		case JUN:
			break;
		case MAR:
			break;
		case MAY:
			break;
		case MODE_1:
			break;
		case MODE_2:
			break;
		case MODE_3:
			break;
		case MODE_4:
			break;
		case MODE_5:
			break;
		case MODE_6:
			break;
		case MODE_7:
			break;
		case MODE_8:
			break;
		case MONDAY:
			break;
		case NOV:
			break;
		case OCT:
			break;
		case OTHERWISE:
			break;
		case RID:
			break;
		case SATURDAY:
			break;
		case SECTION:
			break;
		case SEP:
			break;
		case SID:
			break;
		case SUB_SECTION:
			break;
		case SUB_SUB_SECTION:
			break;
		case SUNDAY:
			break;
		case TEMPLATE:
			for (TemplateNode child : templateNode.getChildren()) {
				childrenContent.append(this.toHtml(child));
			}
			sb.append(this.getTable(this.getTbody(childrenContent.toString())));
			break;
		case THURSDAY:
			break;
		case TITLE:
			break;
		case TUESDAY:
			break;
		case WEDNESDAY:
			break;
		case WHEN_DATE_IS:
			break;
		case WHEN_DAY_NAME_IS:
			break;
		case WHEN_DAY_OF_MONTH_IS:
			break;
		case WHEN_EXISTS:
			break;
		case WHEN_LUKAN_CYCLE_DAY_IS:
			break;
		case WHEN_MODE_OF_WEEK_IS:
			break;
		case WHEN_MONTH_NAME_IS:
			break;
		case WHEN_MOVABLE_CYCLE_DAY_IS:
			break;
		case WHEN_PASCHA:
			break;
		case WHEN_PENTECOSTARIAN_DAY_IS:
			break;
		case WHEN_SUNDAYS_BEFORE_TRIODION:
			break;
		case WHEN_SUNDAY_AFTER_ELEVATION_OF_CROSS_DAY_IS:
			break;
		case WHEN_TRIODION_DAY_IS:
			break;
		default:
			break;
		
		}
		return sb.toString();
	}
	
	public String getContentDiv(String content) {
		StringBuffer sb = new StringBuffer();
		sb.append("div class=\"content\">");
		sb.append(content);
		sb.append("</div>");
		return sb.toString();
	}
	public String getTable(String content) {
		StringBuffer sb = new StringBuffer();
		sb.append("<table id=\"biTable\">");
		sb.append(content);
		sb.append("</table>");
		return sb.toString();
	}
	
	public String getTbody(String content) {
		StringBuffer sb = new StringBuffer();
		sb.append("<tbody");
		sb.append(content);
		sb.append("</tbody>");
		return sb.toString();
	}

	public String getTr(String leftContent, String rightContent) {
		StringBuffer sb = new StringBuffer();
		sb.append("<tr");
		sb.append(leftContent);
		sb.append(rightContent);
		sb.append("</tr>");
		return sb.toString();
	}
	
	public String getTd(String tdClass, String content) {
		StringBuffer sb = new StringBuffer();
		sb.append("<td class=\"");
		sb.append(tdClass);
		sb.append("\">");
		sb.append(content);
		sb.append("</td>");
		return sb.toString();
	}
	
	public String getPara(String paraClass, String content) {
		StringBuffer sb = new StringBuffer();
		sb.append("<p class=\"");
		sb.append(paraClass);
		sb.append("\">");
		sb.append(content);
		sb.append("</p>");
		return sb.toString();
	}

	public String getFormatSpan(String className, String content) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span class=\"");
		sb.append(className);
		sb.append("\">");
		sb.append(content);
		sb.append("</span>");
		return sb.toString();
	}
	public String getKvpSpan(
			String library
			, String topic
			, String key
			, String content
			) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span class=\"kvp\" data-key=\"");
		sb.append(topic);
		sb.append("_");
		sb.append(library);
		sb.append("|");
		sb.append(key);
		sb.append("\">");
		sb.append(content);
		sb.append("</span>");
		return sb.toString();
	}

	public static void main(String[] args) {
		String pathIn = "/Users/mac002/canBeRemoved/latex2olw/meta/";
		String pathOut = "/Users/mac002/canBeRemoved/latex2olw/html/";
		MetaTemplate2Html n = new MetaTemplate2Html();
		n.process(pathIn, pathOut);

	}

}
