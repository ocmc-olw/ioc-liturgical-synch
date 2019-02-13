package org.ocmc.ioc.liturgical.synch.latex;

import java.util.ArrayList;
import java.util.List;

import org.ocmc.ioc.liturgical.schemas.models.supers.AbstractModel;

import com.google.gson.annotations.Expose;

public class MetaCommand extends AbstractModel {
	@Expose String cmdName = "";
	@Expose int argCount = 0;
	@Expose List<String> argFormats = new ArrayList<String>();
	
	public MetaCommand() {
		super();
	}
	public String getCmdName() {
		return cmdName;
	}
	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}
	public List<String> getArgFormats() {
		return argFormats;
	}
	public void setArgFormats(List<String> argFormats) {
		this.argFormats = argFormats;
	}
	public void addArgFormat(String argFormat) {
		this.argFormats.add(argFormat);
	}
	public int getArgCount() {
		return argCount;
	}
	public void setArgCount(int argCount) {
		this.argCount = argCount;
	}
}
