package com.reminmax.pointsapp.domain.model

data class UserMessage(
    val id: Long,
    val message: String,
    val actionLabel: String?,
    val onActionPerformed: () -> Unit,
)