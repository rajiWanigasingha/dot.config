package org.hyprconfig.model

import kotlinx.serialization.Serializable

@Serializable
data class SettingsModel(
    var storeInfo: List<StoreInfoModel>? = null,
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
    var general: List<GeneralModel>? = null,
    var misc: List<MiscModel>? = null,
    var group: List<GroupModel>? = null,
    var debug: List<DebugModel>? = null,
    var decoration: List<DecorationModel>? = null,
    var dwindle: List<DwindleModel>? = null,
    var master: List<MasterModel>? = null,
    var animations: List<AnimationsModel>? = null,
    var inputs: List<InputsModel>? = null,
    var binds: List<BindsModel>? = null,
    var gestures: List<GesturesModel>? = null,
    var xwayland: List<XwaylandModel>? = null,
    var openGL: List<OpenGLModel>? = null,
    var cursor: List<CursorModel>? = null,
    var render: List<RenderModel>? = null,
    var ecosystem: List<EcosystemModel>? = null,
    var experimental: List<ExperimentalModel>? = null,
    var device: List<DevicesModel>? = null
)
