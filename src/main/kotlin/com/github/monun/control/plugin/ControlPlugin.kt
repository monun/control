package com.github.monun.control.plugin

import io.github.monun.kommand.kommand
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

/**
 * @author Monun
 */
class ControlPlugin : JavaPlugin() {
    private lateinit var controlFile: File
    private lateinit var controls: LinkedHashMap<Control<*>, Any>

    @Suppress("UNUSED")
    fun <T> control(cc: Control<T>, option: T) {
        requireNotNull(option) { "Option cannot be null" }
        controls[cc] = option
    }

    fun unsafeControl(cc: Control<*>, option: Any) {
        require(cc.type == option.javaClass) {"Type mismatch"}
        controls[cc] = option
        save()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> control(cc: Control<T>): T = controls[cc] as T

    override fun onEnable() {
        controlFile = File(dataFolder, "controls.yml")
        controls = Control.values.associateWithTo(LinkedHashMap()) { it.default }
        load()

        setupModules()
        setupCommands()
    }

    override fun onDisable() {
        save()
    }

    private fun setupModules() {
        server.pluginManager.registerEvents(EventListener(this), this)
    }

    private fun setupCommands() = kommand {
        KommandCTL.register(this@ControlPlugin, this)
    }

    private fun load() {
        if (!controlFile.exists()) return

        runCatching {
            YamlConfiguration.loadConfiguration(controlFile)
        }.onSuccess { config ->
            for (control in Control.values) {
                val value = config.get(control.name) ?: continue
                if (control.type.isAssignableFrom(value.javaClass)) {
                    controls[control] = value
                } else {
                    logger.warning("Cannot load control ${control.name} - type mismatch")
                }
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    private fun save() {
        YamlConfiguration().apply {
            for ((control, value) in controls) {
                set(control.name, value)
            }
        }.runCatching {
            save(controlFile.also { it.parentFile.mkdirs() })
        }.onFailure {
            it.printStackTrace()
        }
    }
}