package com.clutcher.comments.annotator;

import com.clutcher.comments.highlighter.impl.CommentHighlighter;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractCommentHighlighterAnnotator implements Annotator {

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (isCommentHighlightingElement(element)) {
            final String comment = extractCommentTextFromElement(element);
            int startOffset = element.getTextRange().getStartOffset();

            CommentHighlighter commentHighlighter = ServiceManager.getService(CommentHighlighter.class);

            List<Pair<TextRange, TextAttributesKey>> highlights = commentHighlighter.getHighlights(comment, startOffset);

            for (Pair<TextRange, TextAttributesKey> highlight : highlights) {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(highlight.first)
                        .textAttributes(highlight.second)
                        .create();
            }
        }
    }

    protected String extractCommentTextFromElement(@NotNull PsiElement element) {
        return element.getText();
    }

    protected abstract boolean isCommentHighlightingElement(@NotNull PsiElement element);

}
