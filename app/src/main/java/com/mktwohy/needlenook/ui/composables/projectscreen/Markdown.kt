package com.mktwohy.needlenook.ui.composables.projectscreen

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.intellij.markdown.IElementType
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.accept
import org.intellij.markdown.ast.acceptChildren
import org.intellij.markdown.ast.visitors.Visitor
import org.intellij.markdown.flavours.MarkdownFlavourDescriptor
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

@JvmInline
value class Markdown(val value: String) : CharSequence by value {
    fun parseAST(flavor: MarkdownFlavourDescriptor = Defaults.mdFlavor): ASTNode =
        MarkdownParser(flavor).buildMarkdownTreeFromString(value)

    fun parseHtml(flavor: MarkdownFlavourDescriptor = Defaults.mdFlavor): String =
        HtmlGenerator(value, parseAST(flavor), flavor).generateHtml()

    fun toAnnotatedString(
        flavor: MarkdownFlavourDescriptor = Defaults.mdFlavor,
        elementTypeToSpanStyle: Map<IElementType, SpanStyle> = Defaults.mdElementTypeToSpanStyle,
        elementTypeToParagraphStyle: Map<IElementType, ParagraphStyle> = Defaults.mdElementTypeToParagraphStyle
    ): AnnotatedString {
        val md = this

        return buildAnnotatedString {
            append(md)

            md.parseAST(flavor).traverse { node ->
                elementTypeToSpanStyle[node.type]?.let { style ->
                    addStyle(
                        style = style,
                        start = node.startOffset,
                        end = node.endOffset
                    )
                }
                elementTypeToParagraphStyle[node.type]?.let { style ->
                    addStyle(
                        style = style,
                        start = node.startOffset,
                        end = node.endOffset
                    )
                }
            }
        }
    }

    companion object {
        object Defaults {
            val mdFlavor = CommonMarkFlavourDescriptor()
            val mdElementTypeToSpanStyle = mapOf(
                // ATX: header
                MarkdownElementTypes.ATX_1 to SpanStyle(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Medium
                ),
                MarkdownElementTypes.ATX_2 to SpanStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Medium
                ),
                MarkdownElementTypes.ATX_3 to SpanStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium
                ),
                MarkdownElementTypes.ATX_4 to SpanStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Medium
                ),
                MarkdownElementTypes.ATX_5 to SpanStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium
                ),
                MarkdownElementTypes.ATX_6 to SpanStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                ),

                MarkdownElementTypes.EMPH to SpanStyle(fontStyle = FontStyle.Italic),
                MarkdownElementTypes.STRONG to SpanStyle(fontWeight = FontWeight.Bold),
                MarkdownElementTypes.CODE_SPAN to SpanStyle(fontFamily = FontFamily.Monospace),
                MarkdownTokenTypes.LIST_BULLET to SpanStyle(fontWeight = FontWeight.Light),
                MarkdownTokenTypes.LIST_NUMBER to SpanStyle(fontWeight = FontWeight.Light),
            )
            val mdElementTypeToParagraphStyle = mapOf<IElementType, ParagraphStyle>(

            )
        }
    }
}

private fun ASTNode.traverse(onVisitNode: (ASTNode) -> Unit) {
    accept(
        object : Visitor {
            override fun visitNode(node: ASTNode) {
                onVisitNode(node)
                node.acceptChildren(this)
            }
        }
    )
}