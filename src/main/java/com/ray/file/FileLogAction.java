package com.ray.file;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FileLogAction extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);

    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {

        ToolWindowManager.getInstance(Objects.requireNonNull(event.getProject())).getToolWindow("TxtReader").show(() -> {
        });

        FileChooserDescriptor chooserDescriptor = new FileChooserDescriptor(true,true,true,true,true,true);
        VirtualFile virtualFile = FileChooser.chooseFile(chooserDescriptor, event.getProject(), null);

        if(virtualFile != null) {
            MyToolWin.virtualFile = virtualFile;
        }
        MyToolWin.init();

    }

}
