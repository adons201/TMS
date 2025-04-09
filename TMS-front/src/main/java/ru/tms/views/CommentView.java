package ru.tms.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import ru.tms.dto.CommentDto;
import ru.tms.services.CommentService;

import java.time.LocalDateTime;
import java.util.List;

public class CommentView extends Composite<VerticalLayout> {

    private TextArea commentInput;
    private final CommentService commentServiceClient;
    private final String targetType;
    private final Long targetId;
    private final String author;
    private VerticalLayout commentsLayout;


    public CommentView(CommentService commentServiceClient, String targetType, Long targetId, String author) {
        super();
        this.commentServiceClient = commentServiceClient;
        this.targetType = targetType;
        this.targetId = targetId;
        this.author = author;
        this.initUI();
    }

    private void initUI() {
        commentInput = new TextArea(getTranslation("newComment"));
        Button sendComment = new Button(getTranslation("sendComments"));
        sendComment.addClickListener(event -> {
            if (!commentInput.getValue().isEmpty()) {
                this.addComment();
                Notification.show(getTranslation("commentHasBeenSent") + commentInput.getValue());
                commentInput.clear();
            } else Notification.show(getTranslation("commentCannotBeEmpty"));});
        commentInput.setWidth("40%");
        commentsLayout = new VerticalLayout();
        this.getContent().add(commentInput, sendComment, commentsLayout);
        this.updateComments();
    }

    private void addComment() {
        commentServiceClient.createComment(CommentDto.builder()
                .content(commentInput.getValue())
                .createdAt(LocalDateTime.now())
                .targetObjectId(targetId)
                .targetType(targetType)
                .author(author)
                .build());
        updateComments();
    }

    private void updateComments() {
        List<CommentDto> comments = commentServiceClient.getAllComments(targetType, targetId);
        showComments(comments);
    }

    private void showComments(List<CommentDto> comments) {
        commentsLayout.getElement().getComponent().ifPresent((x)->commentsLayout.removeAll());
        comments.forEach(comment -> {
            Div commentContainer = new Div();
            commentContainer.getElement().setAttribute("style",
                    "position: relative; width: 40%; height: 100%;");
            Span span = new Span(comment.getAuthor() + ": " + comment.getContent());
            span.setWidth("40%");
            span.setMaxWidth("40%");
            span.getElement().getStyle().set("word-break", "break-word");
            Button deleteButton = new Button(VaadinIcon.TRASH.create(),
                    event -> {commentServiceClient.deleteComment(comment.getId());
                        updateComments();});
            deleteButton.setVisible(comment.getAuthor().equals(this.author));
            deleteButton.getElement().setAttribute("style", "float: right; margin-top: 0px;");
            Button editButton = new Button(VaadinIcon.PENCIL.create(),
                    event -> openEditor(comment));
            editButton.setVisible(comment.getAuthor().equals(this.author));
            editButton.getElement().setAttribute("style", "float: right; margin-top: 0px;");
            commentContainer.add(span, deleteButton, editButton);
            commentsLayout.add(commentContainer);
        });
    }

    private void openEditor(CommentDto comment) {
        TextArea editor = new TextArea();
        editor.setValue(comment.getContent());
        editor.setWidthFull();

        Dialog dialog = new Dialog();

        Button saveButton = new Button("Save", event -> saveEditedComment(comment, editor.getValue(), dialog));
        Button cancelButton = new Button("Cancel", event -> closeEditor(dialog));

        HorizontalLayout buttons = new HorizontalLayout(saveButton, cancelButton);
        buttons.setWidthFull();
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        VerticalLayout editorLayout = new VerticalLayout(editor, buttons);
        editorLayout.setWidthFull();

        dialog.add(editorLayout);
        dialog.open();
    }

    private void saveEditedComment(CommentDto comment, String editedContent, Dialog dialog) {
        comment.setContent(editedContent);
        commentServiceClient.updateComment(comment.getId(), comment);
        updateComments();
        closeEditor(dialog);
    }

    private void closeEditor(Dialog dialog) {
        dialog.close();
    }

}
