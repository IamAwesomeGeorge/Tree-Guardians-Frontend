package com.example.tg.models

import kotlin.properties.Delegates

public class TreeModel {
    private var id: Int by Delegates.notNull<Int>()

    private var creation_date: String by Delegates.notNull<String>()
    private var id_user: Int by Delegates.notNull<Int>()
    private var species: String by Delegates.notNull<String>()
    private var latitude: Double by Delegates.notNull<Double>()
    private var longitude: Double by Delegates.notNull<Double>()
    private var health_status: String by Delegates.notNull<String>()
    private var circumference: Double by Delegates.notNull<Double>()
    private var planted: String by Delegates.notNull<String>()
    private var height: Int by Delegates.notNull<Int>()
    private var is_deleted: Boolean by Delegates.notNull<Boolean>()

}