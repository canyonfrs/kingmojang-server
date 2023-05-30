package app.kingmojang.domain

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime

@MappedSuperclass
abstract class SoftDeletable(
    @Column(name = "deleted")
    var deleted: Boolean = false,

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null
) {
    fun delete() {
        this.deleted = true
        this.deletedAt = LocalDateTime.now()
    }
}