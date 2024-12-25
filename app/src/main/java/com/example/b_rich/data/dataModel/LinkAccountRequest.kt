package com.example.b_rich.data.dataModel

data class LinkAccountRequest(
    val rib: String,
    val nickname: String?
) {
    override fun toString(): String {
        return "LinkAccountRequest(rib='$rib', nickname=$nickname)"
    }
}