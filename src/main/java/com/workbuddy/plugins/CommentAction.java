package com.workbuddy.plugins;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.workbuddy.models.CustomDialog;
import org.jetbrains.annotations.NotNull;

public class CommentAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent event) {
        // Using the event, evaluate the context,
        // and enable or disable the action.
    }

    //This method is called when the action is triggered by the user.
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        SelectionModel selectionModel = editor.getSelectionModel();
        if (selectionModel.hasSelection()) {
            String selectedText = selectionModel.getSelectedText();
            CustomDialog dialog = new CustomDialog(selectedText);
            dialog.show();
        }
    }
}
