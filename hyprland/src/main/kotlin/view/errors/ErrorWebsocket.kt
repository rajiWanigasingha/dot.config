package org.dot.config.view.errors

object ErrorWebsocket {

    class InvalidActionTypeForPages() : Exception("Invalid Action Type For /Pages, Valid Action Is SIDE_BAR")

    class InvalidActionTypeForMain() : Exception("Invalid Action Type For /main, Valid Action Is MAIN")

    class InvalidActionTypeForHelp() : Exception("Invalid Action Type For /help, Valid Action Is HELP")
}