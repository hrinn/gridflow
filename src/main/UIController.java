package main;

import java.lang.System;
import java.lang.String;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class UIController {

    private MenuState state = MenuState.MenuExpanded;

    @FXML
    // Expand button reference
    private Button ComponentMenuButton;

    @FXML
    public void UIController(){ }

    @FXML
    private void initialize() { }

    @FXML
    private void printOutput() {
        state = state == MenuState.MenuExpanded ? MenuState.MenuCollapsed : MenuState.MenuExpanded;
        String str = "State of menu is: " + state.name();
        System.out.println(str);
    }



}
