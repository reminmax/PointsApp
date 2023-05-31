package com.reminmax.pointsapp.data.helpers

import android.content.Context
import com.reminmax.pointsapp.domain.resource_provider.IResourceProvider

class AndroidResourceProvider(
    private val context: Context
): IResourceProvider {

    override fun getString(resourceId: Int): String = context.getString(resourceId)

    override fun getString(
        resourceId: Int,
        vararg args: Any
    ): String {
        return if (args.isNotEmpty()) {
            context.resources.getString(resourceId, *args)
        } else {
            context.resources.getString(resourceId)
        }
    }

}
