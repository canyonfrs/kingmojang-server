package app.kingmojang.domain.highlight.repository

import app.kingmojang.domain.highlight.domain.Highlight
import org.springframework.data.jpa.repository.JpaRepository

interface HighlightRepository : JpaRepository<Highlight, Long> {
}