package network

import kotlinx.coroutines.flow.Flow

interface INetworkConnectivityObserver {
    fun observe(): Flow<NetworkStatus>
}

enum class NetworkStatus {
    Available,
    Unavailable,
    Losing,
    Lost
}