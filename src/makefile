ROOT_DIR = mpeg_book_keeper/
WEEKLY_RECAP_DIR = $(ROOT_DIR)weekly_recap/
JCA_BUILDER_DIR = $(ROOT_DIR)jca_builder/
PM_DIR = $(ROOT_DIR)pm_workbook_compiler/
QR_DIR = $(ROOT_DIR)quarterly_reporter/
COMPILER = javac
CLASSPATH = #-classpath .:../lib/jxl.jar

default: \
	$(ROOT_DIR)MPEGBookKeeper.class \
	$(ROOT_DIR)MPEGBookKeeperGui.class \
	$(ROOT_DIR)Settings.class \
	$(ROOT_DIR)SettingsException.class \
	$(ROOT_DIR)SettingsGui.class \
	$(ROOT_DIR)GuiTab.class \
	$(ROOT_DIR)SelectionPanel.class \
	$(ROOT_DIR)TextPanel.class \
	$(ROOT_DIR)SubProcess.class \
	\
	$(WEEKLY_RECAP_DIR)Job.class \
	$(WEEKLY_RECAP_DIR)Recap.class \
	$(WEEKLY_RECAP_DIR)TimeSheetFormatException.class \
	$(WEEKLY_RECAP_DIR)WeeklyRecap.class \
	$(WEEKLY_RECAP_DIR)WeeklyRecapGui.class \
	\
	$(JCA_BUILDER_DIR)JCA.class \
	$(JCA_BUILDER_DIR)JCABuilder.class \
	$(JCA_BUILDER_DIR)JCAColumn.class \
	$(JCA_BUILDER_DIR)JCAException.class \
	$(JCA_BUILDER_DIR)JCANotFoundException.class \
	$(JCA_BUILDER_DIR)JCAGui.class \
	$(JCA_BUILDER_DIR)Job.class \
	$(JCA_BUILDER_DIR)JobSorter.class \
	$(JCA_BUILDER_DIR)Recap.class \
	$(JCA_BUILDER_DIR)RecapFormatException.class \
	$(JCA_BUILDER_DIR)Rejection.class \
	\
	$(PM_DIR)JCAFileName.class \
	$(PM_DIR)JCAFormat.class \
	$(PM_DIR)Job.class \
	$(PM_DIR)PMGui.class \
	$(PM_DIR)PMWorkbook.class \
	$(PM_DIR)PMWorkbookCompiler.class \
	\
	$(QR_DIR)JCA.class \
	$(QR_DIR)JCAException.class \
	$(QR_DIR)JCAFileName.class \
	$(QR_DIR)Project.class \
	$(QR_DIR)QRGui.class \
	$(QR_DIR)QuarterlyReport.class \
	$(QR_DIR)QuarterlyReporter.class \
	$(QR_DIR)Rejection.class \
	$(QR_DIR)StatusFile.class \
	$(QR_DIR)WriteOff.class \
	
#MPEG Book Keeper
$(ROOT_DIR)MPEGBookKeeper.class: $(ROOT_DIR)MPEGBookKeeper.java
	$(COMPILER) $(CLASSPATH) $(ROOT_DIR)MPEGBookKeeper.java

$(ROOT_DIR)MPEGBookKeeperGui.class: $(ROOT_DIR)MPEGBookKeeperGui.java
	$(COMPILER) $(CLASSPATH) $(ROOT_DIR)MPEGBookKeeperGui.java

$(ROOT_DIR)Settings.class: $(ROOT_DIR)Settings.java
	$(COMPILER) $(CLASSPATH) $(ROOT_DIR)Settings.java

$(ROOT_DIR)SettingsException.class: $(ROOT_DIR)SettingsException.java
	$(COMPILER) $(CLASSPATH) $(ROOT_DIR)SettingsException.java

$(ROOT_DIR)SettingsGui.class: $(ROOT_DIR)SettingsGui.java
	$(COMPILER) $(CLASSPATH) $(ROOT_DIR)SettingsGui.java

$(ROOT_DIR)GuiTab.class: $(ROOT_DIR)GuiTab.java
	$(COMPILER) $(CLASSPATH) $(ROOT_DIR)GuiTab.java

$(ROOT_DIR)SelectionPanel.class: $(ROOT_DIR)SelectionPanel.java
	$(COMPILER) $(CLASSPATH) $(ROOT_DIR)SelectionPanel.java

$(ROOT_DIR)TextPanel.class: $(ROOT_DIR)TextPanel.java
	$(COMPILER) $(CLASSPATH) $(ROOT_DIR)TextPanel.java

$(ROOT_DIR)SubProcess.class: $(ROOT_DIR)SubProcess.java
	$(COMPILER) $(CLASSPATH) $(ROOT_DIR)SubProcess.java

#Weekly Recap
$(WEEKLY_RECAP_DIR)Job.class: $(WEEKLY_RECAP_DIR)Job.java
	$(COMPILER) $(CLASSPATH) $(WEEKLY_RECAP_DIR)Job.java

$(WEEKLY_RECAP_DIR)Recap.class: $(WEEKLY_RECAP_DIR)Recap.java
	$(COMPILER) $(CLASSPATH) $(WEEKLY_RECAP_DIR)Recap.java

$(WEEKLY_RECAP_DIR)TimeSheetFormatException.class: $(WEEKLY_RECAP_DIR)TimeSheetFormatException.java
	$(COMPILER) $(CLASSPATH) $(WEEKLY_RECAP_DIR)TimeSheetFormatException.java

$(WEEKLY_RECAP_DIR)WeeklyRecap.class: $(WEEKLY_RECAP_DIR)WeeklyRecap.java
	$(COMPILER) $(CLASSPATH) $(WEEKLY_RECAP_DIR)WeeklyRecap.java

$(WEEKLY_RECAP_DIR)WeeklyRecapGui.class: $(WEEKLY_RECAP_DIR)WeeklyRecapGui.java
	$(COMPILER) $(CLASSPATH) $(WEEKLY_RECAP_DIR)WeeklyRecapGui.java

#JCA Builder
$(JCA_BUILDER_DIR)JCA.class: $(JCA_BUILDER_DIR)JCA.java
	$(COMPILER) $(CLASSPATH) $(JCA_BUILDER_DIR)JCA.java

$(JCA_BUILDER_DIR)JCABuilder.class: $(JCA_BUILDER_DIR)JCABuilder.java
	$(COMPILER) $(CLASSPATH) $(JCA_BUILDER_DIR)JCABuilder.java

$(JCA_BUILDER_DIR)JCAColumn.class: $(JCA_BUILDER_DIR)JCAColumn.java
	$(COMPILER) $(CLASSPATH) $(JCA_BUILDER_DIR)JCAColumn.java

$(JCA_BUILDER_DIR)JCAException.class: $(JCA_BUILDER_DIR)JCAException.java
	$(COMPILER) $(CLASSPATH) $(JCA_BUILDER_DIR)JCAException.java

$(JCA_BUILDER_DIR)JCANotFoundException.class: $(JCA_BUILDER_DIR)JCANotFoundException.java
	$(COMPILER) $(CLASSPATH) $(JCA_BUILDER_DIR)JCANotFoundException.java

$(JCA_BUILDER_DIR)JCAGui.class: $(JCA_BUILDER_DIR)JCAGui.java
	$(COMPILER) $(CLASSPATH) $(JCA_BUILDER_DIR)JCAGui.java

$(JCA_BUILDER_DIR)Job.class: $(JCA_BUILDER_DIR)Job.java
	$(COMPILER) $(CLASSPATH) $(JCA_BUILDER_DIR)Job.java

$(JCA_BUILDER_DIR)JobSorter.class: $(JCA_BUILDER_DIR)JobSorter.java
	$(COMPILER) $(CLASSPATH) $(JCA_BUILDER_DIR)JobSorter.java

$(JCA_BUILDER_DIR)Recap.class: $(JCA_BUILDER_DIR)Recap.java
	$(COMPILER) $(CLASSPATH) $(JCA_BUILDER_DIR)Recap.java

$(JCA_BUILDER_DIR)RecapFormatException.class: $(JCA_BUILDER_DIR)RecapFormatException.java
	$(COMPILER) $(CLASSPATH) $(JCA_BUILDER_DIR)RecapFormatException.java

$(JCA_BUILDER_DIR)Rejection.class: $(JCA_BUILDER_DIR)Rejection.java
	$(COMPILER) $(CLASSPATH) $(JCA_BUILDER_DIR)Rejection.java
	
#PM Workbook Compiler
$(PM_DIR)JCAFileName.class: $(PM_DIR)JCAFileName.java
	$(COMPILER) $(CLASSPATH) $(PM_DIR)JCAFileName.java

$(PM_DIR)JCAFormat.class: $(PM_DIR)JCAFormat.java
	$(COMPILER) $(CLASSPATH) $(PM_DIR)JCAFormat.java

$(PM_DIR)Job.class: $(PM_DIR)Job.java
	$(COMPILER) $(CLASSPATH) $(PM_DIR)Job.java

$(PM_DIR)PMGui.class: $(PM_DIR)PMGui.java
	$(COMPILER) $(CLASSPATH) $(PM_DIR)PMGui.java

$(PM_DIR)PMWorkbook.class: $(PM_DIR)PMWorkbook.java
	$(COMPILER) $(CLASSPATH) $(PM_DIR)PMWorkbook.java

$(PM_DIR)PMWorkbookCompiler.class: $(PM_DIR)PMWorkbookCompiler.java
	$(COMPILER) $(CLASSPATH) $(PM_DIR)PMWorkbookCompiler.java

#Quarterly Reporter
$(QR_DIR)JCA.class: $(QR_DIR)JCA.java
	$(COMPILER) $(CLASSPATH) $(QR_DIR)JCA.java

$(QR_DIR)JCAException.class: $(QR_DIR)JCAException.java
	$(COMPILER) $(CLASSPATH) $(QR_DIR)JCAException.java

$(QR_DIR)JCAFileName.class: $(QR_DIR)JCAFileName.java
	$(COMPILER) $(CLASSPATH) $(QR_DIR)JCAFileName.java

$(QR_DIR)Project.class: $(QR_DIR)Project.java
	$(COMPILER) $(CLASSPATH) $(QR_DIR)Project.java

$(QR_DIR)QRGui.class: $(QR_DIR)QRGui.java
	$(COMPILER) $(CLASSPATH) $(QR_DIR)QRGui.java

$(QR_DIR)QuarterlyReport.class: $(QR_DIR)QuarterlyReport.java
	$(COMPILER) $(CLASSPATH) $(QR_DIR)QuarterlyReport.java

$(QR_DIR)QuarterlyReporter.class: $(QR_DIR)QuarterlyReporter.java
	$(COMPILER) $(CLASSPATH) $(QR_DIR)QuarterlyReporter.java

$(QR_DIR)Rejection.class: $(QR_DIR)Rejection.java
	$(COMPILER) $(CLASSPATH) $(QR_DIR)Rejection.java

$(QR_DIR)StatusFile.class: $(QR_DIR)StatusFile.java
	$(COMPILER) $(CLASSPATH) $(QR_DIR)StatusFile.java

$(QR_DIR)WriteOff.class: $(QR_DIR)WriteOff.java
	$(COMPILER) $(CLASSPATH) $(QR_DIR)WriteOff.java



clean:
	rm $(ROOT_DIR)*.class \
	$(WEEKLY_RECAP_DIR)*.class \
	$(JCA_BUILDER_DIR)*.class \
	$(PM_DIR)*.class \
	$(QR_DIR)*.class \

