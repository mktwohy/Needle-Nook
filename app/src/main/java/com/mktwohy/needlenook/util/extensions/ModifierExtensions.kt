package com.mktwohy.needlenook.util.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role

fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier =
    composed {
        this then Modifier
            .clickable(
                enabled = enabled,
                onClickLabel = onClickLabel,
                role = role,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
    }