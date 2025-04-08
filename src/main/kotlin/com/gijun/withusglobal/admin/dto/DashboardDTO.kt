package com.gijun.withusglobal.admin.dto

class DashboardDTO {
    data class Summary(
        val totalUsers: Int,
        val totalBloggers: Int,
        val totalStores: Int,
        val totalCampaigns: Int,
        val activeCampaigns: Int,
        val pendingCampaigns: Int,
        val totalApplications: Int,
        val approvedApplications: Int,
        val totalReviews: Int,
        val pendingReviews: Int,
        val approvedReviews: Int
    )
    
    data class ChartData(
        val labels: List<String>,
        val datasets: List<Dataset>
    )
    
    data class Dataset(
        val label: String,
        val data: List<Int>,
        val backgroundColor: String? = null,
        val borderColor: String? = null
    )
}
