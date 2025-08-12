package model

import kotlinx.serialization.Serializable
import model.tables.AnimationModel
import model.tables.BezierModel
import model.tables.BindModel
import model.tables.EnvModel
import model.tables.ExecuteModel
import model.tables.LayerRulesModel
import model.tables.MonitorModel
import model.tables.PermissionModel
import model.tables.StandedKeywordModel
import model.tables.SubmapModel
import model.tables.UnbindModel
import model.tables.VariableModel
import model.tables.WindowRulesModel
import model.tables.WorkspaceModel

@Serializable
data class HyprlandSettingsModel(
    var variables: List<VariableModel>? = null,
    var bind: List<BindModel>? = null,
    var monitor: List<MonitorModel>? = null,
    var execute: List<ExecuteModel>? = null,
    var windowRules: List<WindowRulesModel>? = null,
    var workspace: List<WorkspaceModel>? = null,
    var env: List<EnvModel>? = null,
    var layerRules: List<LayerRulesModel>? = null,
    var unbind: List<UnbindModel>? = null,
    var submap: List<SubmapModel>? = null,
    var permission: List<PermissionModel>? = null,
    var bezier: List<BezierModel>? = null,
    var animation: List<AnimationModel>? = null,
    var general: List<StandedKeywordModel>? = null,
    var misc: List<StandedKeywordModel>? = null,
    var group: List<StandedKeywordModel>? = null,
    var debug: List<StandedKeywordModel>? = null,
    var decoration: List<StandedKeywordModel>? = null,
    var dwindle: List<StandedKeywordModel>? = null,
    var master: List<StandedKeywordModel>? = null,
    var animations: List<StandedKeywordModel>? = null,
    var inputs: List<StandedKeywordModel>? = null,
    var binds: List<StandedKeywordModel>? = null,
    var gestures: List<StandedKeywordModel>? = null,
    var xwayland: List<StandedKeywordModel>? = null,
    var openGL: List<StandedKeywordModel>? = null,
    var cursor: List<StandedKeywordModel>? = null,
    var render: List<StandedKeywordModel>? = null,
    var ecosystem: List<StandedKeywordModel>? = null,
    var experimental: List<StandedKeywordModel>? = null,
    var device: List<StandedKeywordModel>? = null
)

@Serializable
data class SuccessRateOfParse(
    val name: String,
    var success: Boolean?
)