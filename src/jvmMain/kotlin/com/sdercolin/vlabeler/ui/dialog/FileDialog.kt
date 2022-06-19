package com.sdercolin.vlabeler.ui.dialog

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.AwtWindow
import java.awt.FileDialog
import java.awt.FileDialog.LOAD
import java.awt.FileDialog.SAVE
import java.awt.Frame
import java.io.FilenameFilter

@Composable
fun OpenFileDialog(
    title: String,
    initialDirectory: String? = null,
    initialFileName: String? = null,
    extensions: List<String>? = null,
    onCloseRequest: (directory: String?, result: String?) -> Unit
) = FileDialog(LOAD, title, initialDirectory, initialFileName, extensions, onCloseRequest)

@Composable
fun SaveFileDialog(
    title: String,
    initialDirectory: String? = null,
    initialFileName: String? = null,
    extensions: List<String>? = null,
    onCloseRequest: (directory: String?, result: String?) -> Unit
) = FileDialog(SAVE, title, initialDirectory, initialFileName, extensions, onCloseRequest)

@Composable
private fun FileDialog(
    mode: Int,
    title: String,
    initialDirectory: String? = null,
    initialFileName: String? = null,
    extensions: List<String>? = null,
    onCloseRequest: (directory: String?, result: String?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(null as Frame?, title, mode) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest(directory, file)
                }
            }
        }.apply {
            initialDirectory?.let { directory = it }
            initialFileName?.let { file = it }
            if (extensions != null) {
                filenameFilter = FilenameFilter { _, name ->
                    extensions.any {
                        name.endsWith(".$it")
                    }
                }
            }
        }
    },
    dispose = FileDialog::dispose
)
