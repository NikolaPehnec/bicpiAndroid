package hr.fer.infosus.bicpi.adapters

class SpinnerItem(id: String, name: String) {

    val id: String = id
    val name: String = name

    override fun toString(): String {
        return name
    }
}