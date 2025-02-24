package ru.tms.components.vaadin.maxime;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vladsch.flexmark.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;

@Tag("markdown-area")
public class MarkdownArea extends Composite<Div> {
    private TextArea input = new TextArea();
    private Link link = new Link("Markdown", "https://vaadin.com/markdown-guide");
    private NativeLabel label = new NativeLabel(" supported");
    private Div writeView;
    private Div previewView;
    private Tab writeTab;
    private Tab previewTab;
    private Tabs tabs;
    Parser parser;
    HtmlRenderer renderer;

    private String value = "";

    public MarkdownArea() {
        this.writeView = new Div(new Component[]{this.input, this.link, this.label});
        this.previewView = new Div();
        this.writeTab = new Tab("Write");
        this.previewTab = new Tab("Preview");
        this.tabs = new Tabs(new Tab[]{this.writeTab, this.previewTab});
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
        this.init();
    }

    public MarkdownArea(String text) {
        this.writeView = new Div(new Component[]{this.input, this.link, this.label});
        this.previewView = new Div();
        this.writeTab = new Tab("Write");
        this.previewTab = new Tab("Preview");
        this.tabs = new Tabs(new Tab[]{this.writeTab, this.previewTab});
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
        if (StringUtil.isNotBlank(text)) {
            this.setValue(text);
        }
        this.init();
    }

    private void init() {
        this.input.setWidth("100%");
        this.link.setTarget("_blank");
        this.previewView.setVisible(false);
        ((Div) this.getContent()).add(new Component[]{this.tabs, this.writeView, this.previewView});
        this.tabs.addSelectedChangeListener((event) -> {
            if (this.tabs.getSelectedTab().getLabel().equals("Preview")) {
                this.writeView.setVisible(false);
                this.previewView.setVisible(true);
                String text = this.getValue().isEmpty() ? "*Nothing to preview*" : this.getValue();
                this.addMarkdown(text);
            } else {
                this.writeView.setVisible(true);
                this.previewView.setVisible(false);
            }
        });
    }

    private void addMarkdown(String value) {
        String html = String.format("<div>%s</div>", this.parseMarkdown(StringUtil.getNullSafeString(value)));
        Html item = new Html(html);
        this.previewView.removeAll();
        this.previewView.add(new Component[]{item});
    }

    private String parseMarkdown(String value) {
        Node text = this.parser.parse(value);
        return "";//this.renderer.render(text);
    }

    public void setValue(String value) {
        this.value = value;
        this.input.setValue(this.value);
    }

    public String getValue() {
        String text = this.getInput().getValue();
        return text.isEmpty()? value : text;
    }

    public TextArea getInput() {
        return this.input;
    }

    public void setMarkdownLink(String markdownLink) {
        this.link.setHref(markdownLink);
    }

    public void editable(Boolean isEditable, String value) {
        setValue(value);
        writeTab.setVisible(isEditable);
        writeView.setVisible(isEditable);
        if (!isEditable) {
            tabs.setSelectedTab(previewTab);
        }
    }
}