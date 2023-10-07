package com.mktwohy.needlenook.ui.composables.projectscreen

import androidx.compose.ui.text.AnnotatedString
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

@JvmInline
value class Markdown(val value: String) : CharSequence by value {
    fun parseAST(flavor: MarkdownFlavourDescriptor = Defaults.mdFlavor): ASTNode =
        MarkdownParser(flavor).buildMarkdownTreeFromString(value)

    fun parseHtml(flavor: MarkdownFlavourDescriptor = Defaults.mdFlavor): String =
        HtmlGenerator(value, parseAST(flavor), flavor).generateHtml()

    fun toAnnotatedString(flavor: MarkdownFlavourDescriptor = Defaults.mdFlavor): AnnotatedString {
        val md = this

        return buildAnnotatedString {
            append(md)

            md.parseAST(flavor).traverse { node ->
                MarkdownStyle.fromIElementType(node.type)?.spanStyle?.let {
                    addStyle(
                        style = it,
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
        }
    }
}

sealed class MarkdownStyle {
    abstract val spanStyle: SpanStyle

    sealed class HeaderStyle(override val spanStyle: SpanStyle) : MarkdownStyle()
    data object H1 : HeaderStyle(SpanStyle(fontSize = 36.sp, fontWeight = FontWeight.Medium))
    data object H2 : HeaderStyle(SpanStyle(fontSize = 32.sp, fontWeight = FontWeight.Medium))
    data object H3 : HeaderStyle(SpanStyle(fontSize = 28.sp, fontWeight = FontWeight.Medium))
    data object H4 : HeaderStyle(SpanStyle(fontSize = 25.sp, fontWeight = FontWeight.Medium))
    data object H5 : HeaderStyle(SpanStyle(fontSize = 22.sp, fontWeight = FontWeight.Medium))
    data object H6 : HeaderStyle(SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Medium))

    sealed class InlineStyle(override val spanStyle: SpanStyle) : MarkdownStyle()
    data object Italic : InlineStyle(SpanStyle(fontStyle = FontStyle.Italic))
    data object Bold : InlineStyle(SpanStyle(fontWeight = FontWeight.Bold))
    data object Code : InlineStyle(SpanStyle(fontFamily = FontFamily.Monospace))

    sealed class ListStyle(override val spanStyle: SpanStyle) : MarkdownStyle()
    data object ListItem : ListStyle(SpanStyle())
    data object ListBullet : ListStyle(SpanStyle(fontWeight = FontWeight.Light))
    data object ListNumber : ListStyle(SpanStyle(fontWeight = FontWeight.Light))

    companion object {
        fun fromIElementType(elementType: IElementType): MarkdownStyle? {
            return when (elementType) {
                MarkdownElementTypes.ATX_1 -> H1
                MarkdownElementTypes.ATX_2 -> H2
                MarkdownElementTypes.ATX_3 -> H3
                MarkdownElementTypes.ATX_4 -> H4
                MarkdownElementTypes.ATX_5 -> H5
                MarkdownElementTypes.ATX_6 -> H6
                MarkdownElementTypes.EMPH -> Italic
                MarkdownElementTypes.STRONG -> Bold
                MarkdownElementTypes.CODE_SPAN -> Code
                MarkdownElementTypes.LIST_ITEM -> ListItem
                MarkdownTokenTypes.LIST_BULLET -> ListBullet
                MarkdownTokenTypes.LIST_NUMBER -> ListNumber
                else -> null
            }
        }
    }
}
