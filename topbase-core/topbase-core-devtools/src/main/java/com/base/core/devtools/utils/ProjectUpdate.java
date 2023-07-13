package com.base.core.devtools.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.gitee.magic.framework.head.utils.IoUtils;

/**
 * 生成项目
 * @author start
 *
 */
public class ProjectUpdate {

	private static final String GITFILENAME=".git";

	private File file;
	private Map<String, String> params;
	
	public ProjectUpdate(File file,Map<String, String> params) {
		this.file=file;
		this.params=params;
	}
	
	public void build() {
		try {
			replaceFileContent(file, this.params);
			renameDirectory(file, this.params);
			System.out.println("项目生成完毕");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void replaceFileContent(File file, Map<String, String> mapper) throws IOException {
		if (GITFILENAME.equals(file.getName())) {
			return;
		}
		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				replaceFileContent(f, mapper);
			} else {
				if(f.getName().endsWith(".xml")
						||f.getName().endsWith(".java")
						||f.getName().endsWith(".md")
						||f.getName().endsWith(".properties")
						||f.getName().endsWith(".sh")) {
					String content = new String(IoUtils.getFileBytes(f));
					for (String key : mapper.keySet()) {
						content = content.replaceAll(key, mapper.get(key));
					}
					BufferedWriter writer = new BufferedWriter(new FileWriter(f));
					writer.write(content);
					writer.flush();
					writer.close();
				}
			}
		}
	}

	public void renameDirectory(File file, Map<String, String> mapper) throws IOException {
		if (GITFILENAME.equals(file.getName())) {
			return;
		}
		File[] files = file.listFiles();
		if (files == null) {
			return;
		}
		for (File f : files) {
			if (f.isDirectory()) {
				renameDirectory(f, mapper);
			}
			String name=f.getName();
			for (String key : mapper.keySet()) {
				if (f.getName().contains(key)) {
					name=name.replace(key, mapper.get(key));
				}
			}
			f.renameTo(new File(file, name));
		}
	}

}
