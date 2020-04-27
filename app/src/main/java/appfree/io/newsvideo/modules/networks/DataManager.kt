package appfree.io.newsvideo.modules.networks

object DataManager {

    private val hashSet = HashSet<String>()

    fun contains(content: String) = hashSet.contains(content)

    fun add(content: String) {
        hashSet.add(content)
    }

}