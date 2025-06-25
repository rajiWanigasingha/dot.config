package org.hyprconfig

import org.hyprconfig.categories.categoriesSettings
import org.hyprconfig.createHyprlandSettings.createHyprlandFile
import org.hyprconfig.errors.NoSettingsFoundInHyprlandPath
import org.hyprconfig.gather.gatherHyprland
import org.hyprconfig.handelProcessSettings.handleBindKeyword
import org.hyprconfig.handelProcessSettings.handleExecute
import org.hyprconfig.handelProcessSettings.handleMonitors
import org.hyprconfig.handelProcessSettings.handleWindowRules
import org.hyprconfig.model.SettingsModel
import org.hyprconfig.handelProcessSettings.handleAnimation
import org.hyprconfig.handelProcessSettings.handleAnimations
import org.hyprconfig.handelProcessSettings.handleBezier
import org.hyprconfig.handelProcessSettings.handleBinds
import org.hyprconfig.handelProcessSettings.handleCursor
import org.hyprconfig.handelProcessSettings.handleDebug
import org.hyprconfig.handelProcessSettings.handleDecoration
import org.hyprconfig.handelProcessSettings.handleDevice
import org.hyprconfig.handelProcessSettings.handleDwindle
import org.hyprconfig.handelProcessSettings.handleEcosystem
import org.hyprconfig.handelProcessSettings.handleEnv
import org.hyprconfig.handelProcessSettings.handleExperimental
import org.hyprconfig.handelProcessSettings.handleGeneral
import org.hyprconfig.handelProcessSettings.handleGestures
import org.hyprconfig.handelProcessSettings.handleGroup
import org.hyprconfig.handelProcessSettings.handleInputs
import org.hyprconfig.handelProcessSettings.handleLayers
import org.hyprconfig.handelProcessSettings.handleMaster
import org.hyprconfig.handelProcessSettings.handleMisc
import org.hyprconfig.handelProcessSettings.handleOpenGl
import org.hyprconfig.handelProcessSettings.handlePermissions
import org.hyprconfig.handelProcessSettings.handleRender
import org.hyprconfig.handelProcessSettings.handleSubmap
import org.hyprconfig.handelProcessSettings.handleUnbind
import org.hyprconfig.handelProcessSettings.handleWorkspaces
import org.hyprconfig.handelProcessSettings.handleXWayland
import org.hyprconfig.helpers.createBackups
import org.hyprconfig.helpers.getTimeForPaths
import org.hyprconfig.variables.parseVariables
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("Parse Hyprland :")

fun parseHyprland(): SettingsModel {

    val processedSettings = SettingsModel()

    logger.info("Begin parsing hyprland")


    // Gathering All Hyprland Settings

    logger.info("Begin gathering of all hyprland settings")

    val allSettings = gatherHyprland()

    logger.info("All Settings has been gathered")

    if (allSettings.isEmpty()) throw NoSettingsFoundInHyprlandPath()

    ////////////////////////////////////

    // Get all variables and store them

    val processedSettingsWithoutVariables = parseVariables(allSettings)

    logger.info("All variables has been turn into original values")

    processedSettings.variables = processedSettingsWithoutVariables.first

    ////////////////////////////////////

    // Filter into categories

    val getCategories = categoriesSettings(processedSettingsWithoutVariables.second)

    logger.info("Sorted into categories")

    ////////////////////////////////////

    logger.info("Handel keyword settings")

    // Parse all key bind settings

    logger.info("Handling keyword bind")

    val bind = handleBindKeyword(getCategories.bind)

    logger.info("All key binds has been processed and there are ${bind.size} bind settings.")

    processedSettings.bind = bind

    ////////////////////////////////////

    // Parse all monitor settings

    logger.info("Handling Monitors")

    val monitor = handleMonitors(getCategories.monitor)

    logger.info("All monitor settings has been processed and there are ${monitor.size} monitor settings.")

    processedSettings.monitor = monitor

    ////////////////////////////////////

    // Parse all executes settings

    logger.info("Handling Executes")

    val executes = handleExecute(getCategories.executes)

    logger.info("All execute settings has been processed and there are ${executes.size} executes settings.")

    processedSettings.execute = executes

    ////////////////////////////////////

    // Parse all windowRule settings

    logger.info("Handling Window")

    val windowRules = handleWindowRules(getCategories.windowrule)

    logger.info("All window rule settings has been processed and there are ${windowRules.size} window rule settings.")

    processedSettings.windowRules = windowRules

    ////////////////////////////////////

    // Parse all workspace settings

    logger.info("Handling Workspace")

    val workspace = handleWorkspaces(getCategories.workspace)

    logger.info("All workspace settings has been processed and there are ${workspace.size} workspace settings.")

    processedSettings.workspace = workspace

    ///////////////////////////////////

    // Parse all environment settings

    logger.info("Handling Env")

    val env = handleEnv(getCategories.env)

    logger.info("All env settings has been processed and there are ${env.size} env settings.")

    processedSettings.env = env

    ///////////////////////////////////

    // Parse all layer rule settings

    logger.info("Handling Layer Rules")

    val layers = handleLayers(getCategories.layerrule)

    logger.info("All layers settings has been processed and there are ${layers.size} layers settings.")

    processedSettings.layerRules = layers

    ///////////////////////////////////

    // Parse all unbind settings

    logger.info("Handling Unbind Rules")

    val unbind = handleUnbind(getCategories.unbind)

    logger.info("All unbind settings has been processed and there are ${unbind.size} unbind settings.")

    processedSettings.unbind = unbind

    ///////////////////////////////////

    // Parse all submap settings

    logger.info("Handling Submap Rules")

    val submap = handleSubmap(getCategories.submap)

    logger.info("All submap settings has been processed and there are ${submap.size} submap settings.")

    processedSettings.submap = submap

    ///////////////////////////////////

    // Parse all permissions settings

    logger.info("Handling Permissions Rules")

    val permissions = handlePermissions(getCategories.permission)

    logger.info("All permissions settings has been processed and there are ${permissions.size} permissions settings.")

    processedSettings.permission = permissions

    ///////////////////////////////////

    // Parse all bezier settings

    logger.info("Handling Bezier Rules")

    val bezier = handleBezier(getCategories.bezier)

    logger.info("All bezier settings has been processed and there are ${bezier.size} bezier settings.")

    processedSettings.bezier = bezier

    ///////////////////////////////////

    // Parse all animation settings

    logger.info("Handling Animation Rules")

    val animation = handleAnimation(getCategories.animation)

    logger.info("All animation settings has been processed and there are ${animation.size} animation settings.")

    processedSettings.animation = animation

    ///////////////////////////////////

    // Parse all general settings

    logger.info("Handling General Rules")

    val general = handleGeneral(getCategories.general)

    logger.info("All general settings has been processed and there are ${general.size} general settings.")

    processedSettings.general = general

    ///////////////////////////////////

    // Parse all misc settings

    logger.info("Handling Misc Rules")

    val misc = handleMisc(getCategories.misc)

    logger.info("All misc settings has been processed and there are ${misc.size} misc settings.")

    processedSettings.misc = misc

    ///////////////////////////////////

    // Parse all group settings

    logger.info("Handling Group Rules")

    val group = handleGroup(getCategories.group)

    logger.info("All group settings has been processed and there are ${group.size} group settings.")

    processedSettings.group = group

    ///////////////////////////////////

    // Parse all debug settings

    logger.info("Handling debug Rules")

    val debug = handleDebug(getCategories.debug)

    logger.info("All debug settings has been processed and there are ${debug.size} debug settings.")

    processedSettings.debug = debug

    ///////////////////////////////////

    // Parse all decoration settings

    logger.info("Handling decoration Rules")

    val decoration = handleDecoration(getCategories.decoration)

    logger.info("All decoration settings has been processed and there are ${decoration.size} decoration settings.")

    processedSettings.decoration = decoration

    ///////////////////////////////////

    // Parse all dwindle layout settings

    logger.info("Handling Dwindle Layout Rules")

    val dwindle = handleDwindle(getCategories.dwindle)

    logger.info("All dwindle settings has been processed and there are ${dwindle.size} dwindle settings.")

    processedSettings.dwindle = dwindle

    ///////////////////////////////////

    // Parse all master layout settings

    logger.info("Handling Master Layout Rules")

    val master = handleMaster(getCategories.master)

    logger.info("All master layout settings has been processed and there are ${master.size} master layout settings.")

    processedSettings.master = master

    ///////////////////////////////////

    // Parse all animations settings

    logger.info("Handling Animations Layout Rules")

    val animations = handleAnimations(getCategories.animations)

    logger.info("All animations settings has been processed and there are ${animations.size} animations layout settings.")

    processedSettings.animations = animations

    ///////////////////////////////////

    // Parse all inputs settings

    logger.info("Handling Inputs Rules")

    val inputs = handleInputs(getCategories.input)

    logger.info("All input settings has been processed and there are ${inputs.size} input settings.")

    processedSettings.inputs = inputs

    ///////////////////////////////////

    // Parse all binds settings

    logger.info("Handling Binds Rules")

    val binds = handleBinds(getCategories.binds)

    logger.info("All binds settings has been processed and there are ${binds.size} binds settings.")

    processedSettings.binds = binds

    ///////////////////////////////////

    // Parse all gestures settings

    logger.info("Handling Gestures Rules")

    val gestures = handleGestures(getCategories.gestures)

    logger.info("All gestures settings has been processed and there are ${gestures.size} gestures settings.")

    processedSettings.gestures = gestures

    ///////////////////////////////////

    // Parse all xWayland settings

    logger.info("Handling Xwayland Rules")

    val xwayland = handleXWayland(getCategories.xwayland)

    logger.info("All xwayland settings has been processed and there are ${xwayland.size} xwayland settings.")

    processedSettings.xwayland = xwayland

    ///////////////////////////////////

    // Parse all openGL settings

    logger.info("Handling OpenGL Rules")

    val openGL = handleOpenGl(getCategories.opengl)

    logger.info("All openGL settings has been processed and there are ${openGL.size} openGL settings.")

    processedSettings.openGL = openGL

    ///////////////////////////////////

    // Parse all cursor settings

    logger.info("Handling Cursor Rules")

    val cursor = handleCursor(getCategories.cursor)

    logger.info("All cursor settings has been processed and there are ${cursor.size} cursor settings.")

    processedSettings.cursor = cursor

    ///////////////////////////////////

    // Parse all render settings

    logger.info("Handling Render Rules")

    val render = handleRender(getCategories.render)

    logger.info("All render settings has been processed and there are ${render.size} render settings.")

    processedSettings.render = render

    ///////////////////////////////////

    // Parse all ecosystem settings

    logger.info("Handling Ecosystem Rules")

    val ecosystem = handleEcosystem(getCategories.ecosystem)

    logger.info("All ecosystem settings has been processed and there are ${ecosystem.size} ecosystem settings.")

    processedSettings.ecosystem = ecosystem

    ///////////////////////////////////

    // Parse all experiment settings

    logger.info("Handling Experimental Rules")

    val experimental = handleExperimental(getCategories.experimental)

    logger.info("All experimental settings has been processed and there are ${experimental.size} experimental settings.")

    processedSettings.experimental = experimental

    ///////////////////////////////////

    // Parse all device settings

    logger.info("Handling Device Rules")

    val device = handleDevice(getCategories.device)

    logger.info("All device settings has been processed and there are ${device.size} device settings.")

    processedSettings.device = device

    ///////////////////////////////////

    // backup existing hyprland file

    createBackups()

    //////////////////////////////////

    // creating hyprland settings file

    createHyprlandFile()

    /////////////////////////////////

    // Getting when settings files been created

    val buildTime = getTimeForPaths()

    processedSettings.storeInfo = buildTime

    /////////////////////////////////

    return processedSettings
}