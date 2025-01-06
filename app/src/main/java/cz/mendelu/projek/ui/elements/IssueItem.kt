package cz.mendelu.projek.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cz.mendelu.projek.R
import cz.mendelu.projek.communication.issue.Issue

@Composable
fun IssueItem(
    issue: Issue,
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier
            .size(150.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        onClick = {
            onClick()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .padding(start = 4.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = issue.title ?: stringResource(R.string.issue_default_title),
                style = MaterialTheme.typography.headlineSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = issue.description ?: stringResource(R.string.issue_default_description),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = issue.priority ?: stringResource(R.string.issue_default_priority),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Red
                )
            }
        }
    }
}
