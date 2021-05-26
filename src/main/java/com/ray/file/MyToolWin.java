package com.ray.file;

import com.ray.file.common.Context;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//import java.awt.*;

/**
 * Created by zrc on 2020-12-10.
 */
public class MyToolWin implements ToolWindowFactory {

    public static VirtualFile virtualFile;

    public static int readIndex = 1;
    public static Map<Integer, List<String>> contentMap = new ConcurrentHashMap<>();

    public static JBSplitter splitter;

    public static ConsoleView consoleView;
    private static DefaultListModel<Integer> defaultListModel;
    private static JBList<Integer> list;

    private static Project project;


    public static JComponent createConsolePanel(ConsoleView view) {
        splitter = new JBSplitter();
        splitter.setProportion(0.2f);

        defaultListModel = new DefaultListModel<>();

        list = new JBList<>(defaultListModel);

        // 修饰每一行的元素
        ColoredListCellRenderer<Integer> coloredListCellRenderer = new ColoredListCellRenderer<Integer>() {
            @Override
            protected void customizeCellRenderer(@NotNull JList<? extends Integer> list, Integer value, int index, boolean selected, boolean hasFocus) {
                int start = (value-1)*1000;
                int end = start + 1000;
                append(start + "-" + end);
                if(selected&&readIndex!=value){
                    readIndex = value;
                    display();
                }
            }
        };
        list.setCellRenderer(coloredListCellRenderer);


        // 增加工具栏（新增按钮、删除按钮、上移按钮、下移按钮）
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(list);
        // 自定义按钮
        decorator.setEditAction(actionButton ->updateAction());

        splitter.setFirstComponent(decorator.createPanel());

        JPanel secondPanel = new JPanel();
        secondPanel.add(view.getComponent(), Component.LEFT_ALIGNMENT);
        secondPanel.setLayout(new GridLayout(1, 1));
        splitter.setSecondComponent(secondPanel);


        // 记录当前俩个组件的比例，存放到map中，key即为该值
        splitter.setSplitterProportionKey("customProportionKey");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(splitter, BorderLayout.CENTER);

        return panel;

    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.project = project;
        TextConsoleBuilder consoleBuilder = TextConsoleBuilderFactory.getInstance().createBuilder(project);
        consoleView = consoleBuilder.getConsole();
        JComponent consolePanel = createConsolePanel(consoleView);
        Content content = toolWindow.getContentManager().getFactory().createContent(consolePanel, "file", false);
        toolWindow.getContentManager().addContent(content);
        new ToolWindowConsole(toolWindow, consoleView, project);
    }

    private static void updateAction() {
        String newItem = Messages.showInputDialog("填入（UTF-8、GBK等）", "修改编码集", Messages.getInformationIcon());
        if (newItem!=null&&newItem.length()>0) {
            Context.charset = newItem;
            init();
        }
    }

    public static void init() {
        readFile();
        contentMap.forEach((index, action) -> {
            defaultListModel.addElement(index);
        });
        display();
    }

    public static void display() {
        consoleView.clear();
        contentMap.get(readIndex).forEach(ToolWindowConsole::log);
        consoleView.scrollTo(0);
    }

    public static void readFile(){
        WriteCommandAction.runWriteCommandAction(project, () -> {
            InputStream in = null;
            MyToolWin.contentMap = new ConcurrentHashMap<>();
            try {
                in = virtualFile.getInputStream();
                BufferedInputStream fis = new BufferedInputStream(in);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis,Context.charset), 10 * 1024);// 用10K的缓冲读取文本文件
                String line = "";
                int count = 0;
                List<String> contentList = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    if(count%1000==0&&count!=0){
                        MyToolWin.contentMap.put(count/1000,contentList);
                        contentList = new ArrayList<>();
                    }
                    count++;
                    contentList.add(line);
                }
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

    }
}
