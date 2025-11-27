package com.example.presentation.utils.error

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.component.theme.PetbulanceTheme
import com.example.presentation.component.ui.Space16
import com.example.presentation.component.ui.atom.BasicButton
import com.example.presentation.component.ui.atom.BasicDialog
import com.example.presentation.component.ui.atom.ButtonType

@Composable
fun ErrorDialog(
    directErrorTitle: String = "오류 발생!",
    directErrorMessage: String? = null,
    errorDialogState: ErrorDialogState,
    errorHandler: (Any?) -> Unit
) {
    errorDialogState.logErrorEvent()
    BasicDialog {
        Text(
            text = directErrorTitle,
            style = MaterialTheme.typography.headlineMedium,
            color = PetbulanceTheme.colorScheme.commonText,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = directErrorMessage ?: errorDialogState.userMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = PetbulanceTheme.colorScheme.descriptionText,
            softWrap = true,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )

        Space16()

        BasicButton(
            text = "닫기",
            type = ButtonType.PRIMARY,
            onClicked = { errorHandler(null) }
        )
    }
}

@Preview(apiLevel = 34)
@Composable
private fun ErrorDialogPreview() {
    PetbulanceTheme {
        ErrorDialog(
            errorDialogState = ErrorDialogState(
                userMessage = "뭔가 문제생긴 것 같음 ㄷㄷㄷ",
                exceptionMessage = "java.lang.NullPointerException: Attempt to invoke virtual method 'int java.lang.String.length()' on a null object reference\n" +
                        "\tat com.example.SiriaTemplate.ui.study.StudyViewModel.processWord(StudyViewModel.kt:42)\n" +
                        "\tat com.example.SiriaTemplate.ui.study.StudyViewModel.onIntent(StudyViewModel.kt:27)\n" +
                        "\tat com.example.SiriaTemplate.ui.study.StudyScreenKt\$StudyScreen$1.invoke(StudyScreen.kt:88)\n",
                isErrorDialogVisible = true
            ),
            errorHandler = {}
        )
    }
}