package app.pillion.protocol

/** NaviLite frameType for messages the phone sends to the bike (observed on the wire). */
internal const val FRAME_TYPE_PHONE: Int = 6

/** NaviLite payloadDataType values. */
internal const val PDT_VALUE: Int = 0
internal const val PDT_POINTER: Int = 1

/** NaviLite service-type identifiers (the message catalogue we use). */
internal object ServiceType {
    // handshake
    const val ESN_UPDATE = 66
    const val ESN_ACK = 81
    const val AUTH_REQUEST = 33
    const val AUTH_ACK = 82
    const val SEC_DATA = 83
    const val SEC_DATA_ACK = 84
    // image channel
    const val IMAGE = 0
    const val IMAGE_ACK = 80
    // post-auth setup burst
    const val NAV_STATUS = 2
    const val DAY_NIGHT = 31
    const val HOME = 10
    const val OFFICE = 11
    const val GPS = 13
    const val APP_SETTING = 12
    const val ZOOM = 14
    const val ROAD = 3
    const val SPEED_LIMIT = 17

    // dash -> phone: "start showing nav image content" (contentType 01 00 = NAVI_IMAGE). The dash
    // expects the setup burst as the reply; without it, it gives up on content after a few seconds
    // (observed ~5s) and sends APP_STOP_CONTENT_UPDATE_REQUEST, even while IMAGE_FRAME_UPDATE keeps
    // getting ACKed. See Handshake.sendContentBurst and docs/PROTOCOL.md service 55.
    const val APP_START_CONTENT_UPDATE_REQUEST = 55

    // dash -> phone requests, unhandled by Pillion today (see docs/PROTOCOL.md "Dashboard Commands").
    // Named here only for readable diagnostics logging — Pillion doesn't act on any of these.
    private val KNOWN_NAMES = mapOf(
        IMAGE to "IMAGE_FRAME_UPDATE",
        IMAGE_ACK to "IMAGE_ACK",
        ESN_UPDATE to "ESN_UPDATE",
        ESN_ACK to "ESN_ACK",
        AUTH_REQUEST to "AUTH_REQUEST",
        AUTH_ACK to "AUTH_REQUEST_ACK",
        SEC_DATA to "AUTH_REQUEST_SEC_DATA",
        SEC_DATA_ACK to "AUTH_REQUEST_SEC_DATA_ACK",
        48 to "APP_START_ROUTE_REQUEST",
        49 to "APP_STOP_ROUTE_REQUEST",
        50 to "APP_SKIP_NEXT_WAYPOINT_REQUEST",
        51 to "APP_MAP_ZOOM_IN_REQUEST",
        52 to "APP_MAP_ZOOM_OUT_REQUEST",
        53 to "APP_GO_HOME_REQUEST",
        54 to "APP_GO_OFFICE_REQUEST",
        APP_START_CONTENT_UPDATE_REQUEST to "APP_START_CONTENT_UPDATE_REQUEST",
        56 to "APP_STOP_CONTENT_UPDATE_REQUEST",
        65 to "MCU_VEHICLE_SPEED_UPDATE",
        70 to "MCU_DIALOG_USER_SELECT_UPDATE",
    )

    /** Human-readable name for diagnostics logging; falls back to the raw id. */
    fun nameFor(serviceType: Int): String = KNOWN_NAMES[serviceType] ?: "UNKNOWN($serviceType)"
}
