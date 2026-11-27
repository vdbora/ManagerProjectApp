package com.example.managerproject.ui.navigation

/**
 * All screens of the app and their routes.
 *
 * Routes pass only IDs (never whole objects). Each screen's ViewModel loads its own data
 * from the repository using that ID, so a screen can always rebuild itself after
 * rotation or after Android kills the process.
 */
object AppDestinations {

    /** First-launch introduction. */
    const val ONBOARDING = "onboarding"

    /** Home: list of boards (start destination for returning users). */
    const val BOARDS = "boards"

    /** One board with its columns. */
    const val BOARD = "board/{${Args.BOARD_ID}}"
    fun board(boardId: Long) = "board/$boardId"

    /** Details of one card. */
    const val CARD_DETAIL = "card/{${Args.CARD_ID}}"
    fun cardDetail(cardId: Long) = "card/$cardId"

    /**
     * Add or edit a card. Optional arguments:
     * new card  -> boardId + columnId (cardId = -1)
     * edit card -> cardId
     */
    const val CARD_EDIT =
        "edit?${Args.BOARD_ID}={${Args.BOARD_ID}}&${Args.COLUMN_ID}={${Args.COLUMN_ID}}&${Args.CARD_ID}={${Args.CARD_ID}}"
    fun newCard(boardId: Long, columnId: Long) = "edit?${Args.BOARD_ID}=$boardId&${Args.COLUMN_ID}=$columnId&${Args.CARD_ID}=-1"
    fun editCard(cardId: Long) = "edit?${Args.CARD_ID}=$cardId"

    /** In-app camera (CameraX). */
    const val CAMERA = "camera"

    /** App settings. */
    const val SETTINGS = "settings"

    /** Names of the navigation arguments. */
    object Args {
        const val BOARD_ID = "boardId"
        const val COLUMN_ID = "columnId"
        const val CARD_ID = "cardId"

        /** Key used to send the captured photo path back from Camera to the form. */
        const val PHOTO_PATH = "photoPath"
    }

    /** Value used when an optional ID argument is not given. */
    const val NO_ID = -1L
}
