package tumble.app.tumble.domain.models.presentation

import tumble.app.tumble.domain.models.network.NetworkResponse
import java.util.Date

data class ResourceSelectionModel (
    val resource: NetworkResponse.KronoxResourceElement,
    val date: Date
)