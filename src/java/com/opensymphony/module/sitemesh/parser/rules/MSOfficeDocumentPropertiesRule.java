package com.opensymphony.module.sitemesh.parser.rules;

import com.opensymphony.module.sitemesh.HTMLPage;
import com.opensymphony.module.sitemesh.html.BasicRule;
import com.opensymphony.module.sitemesh.html.Tag;
import com.opensymphony.module.sitemesh.html.BlockExtractingRule;

/**
 * Extracts the extra properties saved in HTML from MS Office applications (Word and Excel),
 * such as Author, Company, Version, etc.
 *
 * @author Joe Walnes
 */
public class MSOfficeDocumentPropertiesRule extends BlockExtractingRule {

    private final HTMLPage page;
    private boolean inDocumentProperties;

    public MSOfficeDocumentPropertiesRule(HTMLPage page) {
        super(true);
        this.page = page;
    }

    public boolean shouldProcess(String name) {
        return (inDocumentProperties && name.startsWith("o:")) || name.equals("o:DocumentProperties");
    }

    public void process(Tag tag) {
        if (tag.getName().equals("o:DocumentProperties")) {
            inDocumentProperties = (tag.getType() == Tag.OPEN);
            tag.writeTo(context.currentBuffer());
        } else {
            super.process(tag);
        }
    }

    protected void start(Tag tag) {
    }

    protected void end(Tag tag) {
        String name = tag.getName().substring(2);
        page.addProperty("office.DocumentProperties." + name, context.currentBuffer().toString());
        context.mergeBuffer();
    }

}