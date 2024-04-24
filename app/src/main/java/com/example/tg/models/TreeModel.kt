package com.example.tg.models
import com.google.gson.annotations.SerializedName


// This model defines the tree object which we will use in the application and uses
// the SerializedName annotation to convert from JSON to a java object.
data class TreeModel(
    @SerializedName("id") val id: Int,
    @SerializedName("creation_date") val creationDate: String,
    @SerializedName("id_user") val idUser: String,
    @SerializedName("species") val species: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("health_status") val healthStatus: String,
    @SerializedName("circumference") val circumference: Double,
    @SerializedName("planted") val planted: String,
    @SerializedName("height") val height: Int,
    @SerializedName("is_deleted") val isDeleted: Int // MySql is storing as TinyInt not Boolean
    // We could consider building a custom deserializer to correctly convert tiny int
    // to Java Boolean - Tom
)
