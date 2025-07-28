package org.dot.config.controller.ui.customSettingsControllers

import org.dot.config.controller.services.WriteIntoHyprland
import org.dot.config.controller.ui.SidebarController
import org.dot.config.model.Tables
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.convert
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.api.with
import org.jetbrains.kotlinx.dataframe.io.writeCsv
import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.writeText

class AnimationController {

    private val logger = LoggerFactory.getLogger(javaClass.name)
    private val pathAnimation = "${System.getProperty("user.home")}/.dot.config/data/animation.csv"
    private val pathBezier = "${System.getProperty("user.home")}/.dot.config/data/bezier.csv"

    fun addNewAnimation(animation: Tables.AnimationTable ,curve: Tables.BezierTable? = null): Boolean {
        logger.info("Add New Animation")

        val data = SidebarController().getAnimation()

        val animationTree = data.first.toMutableList()

        val bezierCurve = data.second.toMutableList()

        if (curve !== null) {
            bezierCurve.add(curve)

            val curveDF = bezierCurve.toDataFrame().convert { all() }.with { it.toString() }

            writeAllToCsv(animationAndCurves = curveDF , path = pathBezier)

            writeIntoHyprland(animation = null, bezier = bezierCurve , curve = true)
        }


        animationTree.add(animation)

        val animationDF = animationTree.toDataFrame().convert { all() }.with { it.toString() }

        writeAllToCsv(animationAndCurves = animationDF , path = pathAnimation)

        writeIntoHyprland(animation = animationTree , bezier = null , curve = false)

        return true
    }

    fun disableOrEnable(animation: Tables.AnimationTable): Boolean {
        logger.info("Enable Or Diable Animation")

        val data = SidebarController().getAnimation()

        val animationTree = data.first.toMutableList()

        animationTree.forEach {
            if (it.name == animation.name) {
                it.onOff = animation.onOff
            }
        }

        val animationDF = animationTree.toDataFrame().convert { all() }.with { it.toString() }

        writeAllToCsv(animationAndCurves = animationDF , path = pathAnimation)

        writeIntoHyprland(animation = animationTree , bezier = null , curve = false)

        return true
    }

    fun edit(animation: Tables.AnimationTable): Boolean {
        logger.info("Edit Animations")

        val data = SidebarController().getAnimation()

        val animationTree = data.first.toMutableList()

        animationTree.forEach {
            if (it.name == animation.name) {
                it.onOff = animation.onOff
                it.speed = animation.speed
                it.curve = animation.curve
                it.style = animation.style
            }
        }

        val animationDF = animationTree.toDataFrame().convert { all() }.with { it.toString() }

        writeAllToCsv(animationAndCurves = animationDF , path = pathAnimation)

        writeIntoHyprland(animation = animationTree , bezier = null , curve = false)

        return true
    }

    fun delete(animation: Tables.AnimationTable): Boolean {
        logger.info("Delete Animation")

        val data = SidebarController().getAnimation()

        val animationTree = data.first.toMutableList()

        val filteredAnimation = animationTree.filter { it.name != animation.name }

        val animationDF = filteredAnimation.toDataFrame().convert { all() }.with { it.toString() }

        writeAllToCsv(animationAndCurves = animationDF , path = pathAnimation)

        writeIntoHyprland(animation = filteredAnimation , bezier = null , curve = false)

        return true
    }

    fun curveAdd(curve: Tables.BezierTable): Boolean {
        logger.info("Add New Curve")

        val data = SidebarController().getAnimation()

        val curves = data.second.toMutableList()

        curves.add(curve)

        val curveDF = curves.toDataFrame().convert { all() }.with { it.toString() }

        writeAllToCsv(animationAndCurves = curveDF , path = pathBezier)

        writeIntoHyprland(animation = null , bezier = curves , curve = true)

        return true
    }

    fun curveEdit(curve: Tables.BezierTable ,old: Tables.BezierTable): Boolean {
        logger.info("Edit Curve")

        val data = SidebarController().getAnimation()

        val curves = data.second.toMutableList()

        val filteredCurve = curves.map {
            if (it.name == old.name) {
                curve
            }else {
                it
            }
        }

        val curveDF = filteredCurve.toDataFrame().convert { all() }.with { it.toString() }

        writeAllToCsv(animationAndCurves = curveDF , path = pathBezier)

        writeIntoHyprland(animation = null , bezier = filteredCurve , curve = true)

        return true
    }

    fun curveDelete(curve: Tables.BezierTable): Boolean {
        logger.info("Delete Curves")

        val data = SidebarController().getAnimation()

        val animationTree = data.first.toMutableList()

        val bezierCurve = data.second.toMutableList()

        val newAnimations = animationTree.filter { it.curve != curve.name }

        val newBezier = bezierCurve.filter { it.name != curve.name }

        // write animations
        val animationDF = newAnimations.toDataFrame().convert { all() }.with { it.toString() }

        writeAllToCsv(animationAndCurves = animationDF , path = pathAnimation)

        writeIntoHyprland(animation = newAnimations , bezier = null , curve = false)

        // write bezier
        val bezierDF = newBezier.toDataFrame().convert { all() }.with { it.toString() }

        writeAllToCsv(animationAndCurves = bezierDF , path = pathBezier)

        writeIntoHyprland(animation = null , bezier = newBezier , curve = true)

        return true
    }

    private fun writeAllToCsv(animationAndCurves: DataFrame<*> ,path: String) {
        animationAndCurves.writeCsv(path)
    }

    private fun writeIntoHyprland(animation: List<Tables.AnimationTable>?, bezier: List<Tables.BezierTable>?, curve: Boolean) {

        if (curve) {
            logger.info("Write Bezier Curve Into Hyprland")

            if (bezier == null) {
                throw IllegalArgumentException("Bezier Curve Is Empty")
            }

            val value = WriteIntoHyprland().writeBezier(bezier = bezier)

            val hyprlandPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/bezier.conf"

            Path.of(hyprlandPath).writeText(text = value)

            WriteIntoHyprland().updateTime(hyprlandPath = hyprlandPath)
        } else {
            logger.info("Write Animation Into Hyprland")

            if (animation == null) {
                throw IllegalArgumentException("Animation Is Empty")
            }

            val value = WriteIntoHyprland().writeAnimation(animation = animation)

            val hyprlandPath = "${System.getProperty("user.home")}/.config/hypr/hyprConfigAutoGen/animation.conf"

            Path.of(hyprlandPath).writeText(text = value)

            WriteIntoHyprland().updateTime(hyprlandPath = hyprlandPath)
        }
    }

}