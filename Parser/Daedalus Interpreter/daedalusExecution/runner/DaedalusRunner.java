package daedalusExecution.runner;

import daedalusCodeComponents.DaedalusCompilationUnit;
import daedalusCodeComponents.DaedalusSyntaxElement;
import daedalusCodeComponents.statements.DaedalusStatement;
import daedalusExecution.exception.DaedalusException;
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
		
		//TODO Set Base Layer
		
		//File Layer
		executionContext.pushLayer(new DaedalusExecutionLayer());
	}
	
	
	public static void run(String[] args) {
		if(!isInitialized) {
			return;
		}
		
		System.out.println(complationUnit);
		
		//TODO Add Imports
		for(int i=0;i<complationUnit.statementCount();i++) {
			DaedalusSyntaxElement elem = complationUnit.getStatement(i);
			
			try {
				((DaedalusStatement) elem).resolve();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
