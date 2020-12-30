package com.ray.file;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ToolWindowConsole {
    private static Project project;
    private static ConsoleView console;

    private static ToolWindowConsole that;


    public ToolWindowConsole(ToolWindow toolWindow, ConsoleView console, Project project) {
        this.console = console;
        this.project = project;
        this.that = this;
    }

    public static void log(String s) {
        if (s == null || console == null || console.isOutputPaused()) {
            return;
        }
        s = s.trim();

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        console.print(dateTimeFormatter.format(localDateTime) + "  ", ConsoleViewContentType.NORMAL_OUTPUT);
        console.print("INFO", ConsoleViewContentType.LOG_INFO_OUTPUT);
        console.print("  41701  ", ConsoleViewContentType.LOG_WARNING_OUTPUT);
        console.print("--- [   scheduling-1]   ", ConsoleViewContentType.NORMAL_OUTPUT);
        console.print(that.getClass().getCanonicalName() + "  :", ConsoleViewContentType.NORMAL_OUTPUT);
        console.print(s + "\n", ConsoleViewContentType.NORMAL_OUTPUT);
        //console.setOutputPaused(true);
        return;
    }
}
