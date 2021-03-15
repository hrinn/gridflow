package base;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.application.HostServices;

public class AboutPageController {

    public HostServices services;

    public void AboutPageController (HostServices services) {
        this.services = services;
    }

    public void setServices (HostServices services) { this.services = services; }

    @FXML
    private Hyperlink GithubLink;

    @FXML
    private Hyperlink WikiLink;

    public void openGithub () {
        services.showDocument(GithubLink.getText());
    }

    public void openWiki () {
        services.showDocument(WikiLink.getText());
    }

}
