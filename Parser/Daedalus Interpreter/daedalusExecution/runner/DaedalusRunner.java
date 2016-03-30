package daedalusExecution.runner;

import daedalusCodeComponents.DaedalusCompilationUnit;
import daedalusExecution.importer.DaedalusImporter;
import daedalusExecution.types.DaedalusCastResolver;

public class DaedalusRunner {
	public static DaedalusExecutionContext executionContext;
	public static DaedalusCompilationUnit complationUnit;
	public static DaedalusCastResolver castResolver;
	public static DaedalusImporter importer;
	private static boolean isInitialized = false;
	
	public static void init(DaedalusCompilationUnit unit) {
		complationUnit = unit;
		executionContext = new DaedalusExecutionContext();
		castResolver = new DaedalusCastResolver();
		importer = new DaedalusImporter();
		isInitialized = true;
	}
	
	
	public static void run(String[] args) {
		if(!isInitialized) {
			return;
		} 
	}
	
}
