package plugin;

import client.Client;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowManager;

import java.net.Socket;
import java.util.Objects;

/**
 * Created by zrc on 2021-02-25.
 */
public class CartStartAction extends AnAction {

    @Override
    public void update(AnActionEvent e) {
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);

    }
    @Override
    public void actionPerformed(AnActionEvent e) {
        ToolWindowManager.getInstance(Objects.requireNonNull(e.getProject())).getToolWindow("card").show(() -> {
        });
        Client client = new Client();
        Socket socket = client.getSocket();
        if(socket!=null){
            Client.connected = true;
        }
    }
}
