package org.dot.config.controller.ui

import org.dot.config.view.builderComponents.Sidebar
import org.slf4j.LoggerFactory

class HelpPageController {

    private val logger = LoggerFactory.getLogger(javaClass.name)

    fun getHelpUI(actionLinks: Sidebar.ActionLinks ,category: String ,name: String): String {
        logger.info("Getting Help From ${actionLinks.name}/$category:$name.md file")

        val content = object {}.javaClass.getResource("/help/${actionLinks.name}/${category}:${name}.md")?.readText()

        return content ?: ""
    }

}