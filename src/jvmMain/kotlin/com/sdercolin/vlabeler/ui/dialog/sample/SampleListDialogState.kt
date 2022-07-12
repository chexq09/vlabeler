package com.sdercolin.vlabeler.ui.dialog.sample

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.sdercolin.vlabeler.model.Project
import com.sdercolin.vlabeler.ui.editor.EditorState
import com.sdercolin.vlabeler.ui.editor.IndexedEntry
import java.io.File

class SampleListDialogState(
    private val editorState: EditorState
) {
    var selectedSampleName: String? by mutableStateOf(null)
        private set

    var selectedEntryIndex: Int? by mutableStateOf(null)
        private set

    var includedSampleItems: List<SampleListDialogItem.IncludedSample> by mutableStateOf(listOf())
        private set

    var excludedSampleItems: List<SampleListDialogItem.ExcludedSample> by mutableStateOf(listOf())
        private set

    var entryItems: List<SampleListDialogItem.Entry> by mutableStateOf(listOf())
        private set

    init {
        fetch()
    }

    private fun getExistingSampleFiles() = File(editorState.project.sampleDirectory).listFiles().orEmpty()
        .filter { it.extension == Project.SampleFileExtension }
        .map { it.nameWithoutExtension }

    private fun getProjectSampleFilesWithEntries() = editorState.project.entries.groupBy { it.sample }

    private fun fetch() {
        val existing = getExistingSampleFiles()
        val projectSamplesWithEntries = getProjectSampleFilesWithEntries()
        val projectSamples = projectSamplesWithEntries.map { it.key }
        includedSampleItems = projectSamplesWithEntries.map {
            SampleListDialogItem.IncludedSample(
                name = it.key,
                valid = it.key in existing,
                entryCount = it.value.size
            )
        }
        excludedSampleItems = (existing - projectSamples.toSet()).map { SampleListDialogItem.ExcludedSample(it) }
        selectedSampleName?.let { select(it) }
    }

    fun select(name: String) {
        selectedSampleName = name
        val entries = editorState.project.entries
        entryItems = entries.indices.filter { entries[it].sample == name }.map {
            val indexedEntry = IndexedEntry(entries[it], it)
            SampleListDialogItem.Entry(name = indexedEntry.name, entry = indexedEntry)
        }
    }

    fun createDefaultEntry() {
        val sampleName = requireNotNull(selectedSampleName)
        editorState.createDefaultEntry(sampleName)
        fetch()
    }
}
