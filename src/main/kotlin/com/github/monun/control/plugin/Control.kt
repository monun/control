package com.github.monun.control.plugin

import com.google.common.collect.ImmutableList

sealed class Control<T>(val name: String, val type: Class<*>, val default: T) {
    object Chat : Control<Boolean>("chat", java.lang.Boolean::class.java, true)
    object Command : Control<Boolean>("command", java.lang.Boolean::class.java, true)
    object Movement : Control<Boolean>("movement", java.lang.Boolean::class.java, true)
    object Attack : Control<Boolean>("attack", java.lang.Boolean::class.java, true)
    object Damage : Control<Boolean>("damage", java.lang.Boolean::class.java, true)
    object Interaction : Control<Boolean>("interaction", java.lang.Boolean::class.java, true)
    object Breaking : Control<Boolean>("breaking", java.lang.Boolean::class.java, true)
    object Placement : Control<Boolean>("placement", java.lang.Boolean::class.java, true)
    object Crafting : Control<Boolean>("crafting", java.lang.Boolean::class.java, true)
    object Drop : Control<Boolean>("drop", java.lang.Boolean::class.java, true)
    object Pickup : Control<Boolean>("pickup", java.lang.Boolean::class.java, true)
    object JoinMessage : Control<Boolean>("join-message", java.lang.Boolean::class.java, true)
    object QuitMessage : Control<Boolean>("quit-message", java.lang.Boolean::class.java, true)

    val permission = "control.$name"

    @Suppress("UNCHECKED_CAST")
    fun parse(s: String): T? {
        if (type == java.lang.Boolean::class.java) {
            if (s.equals("true", true)) return java.lang.Boolean.TRUE as T
            if (s.equals("false", true)) return java.lang.Boolean.FALSE as T
        } else if (type == String::class.java) {
            return s as T
        }

        return null
    }

    companion object {
        val values: ImmutableList<Control<Boolean>> by lazy {
            ImmutableList.of(
                Chat,
                Command,
                Movement,
                Attack,
                Damage,
                Interaction,
                Breaking,
                Placement,
                Crafting,
                Drop,
                Pickup,
                JoinMessage,
                QuitMessage
            )
        }
    }
}
