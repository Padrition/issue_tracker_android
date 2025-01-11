package cz.mendelu.projek.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cz.mendelu.projek.R
import cz.mendelu.projek.communication.board.Category
import cz.mendelu.projek.communication.issue.Issue
import cz.mendelu.projek.ui.theme.MPLUSRounded1C
import cz.mendelu.projek.utils.parseColor

@Composable
fun CategoryBoard(
    category: Category,
    issues: List<Issue>,
    onIssueClick: (String) -> Unit,
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = parseColor(category.color ?: "#123456"),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ){
            Text(
                text = category.name ?: stringResource(R.string.category_default_name),
                fontFamily = MPLUSRounded1C,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Card (
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(8.dp)
        ){

            if(issues.isEmpty()){

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ){
                    Text(
                        text = stringResource(R.string.no_issue_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

            }else{
                LazyRow {
                    issues.forEach { issue ->
                        item {
                            IssueItem(
                                issue
                            ) {
                                onIssueClick(issue.id!!.oid!!)
                            }
                        }
                    }
                }
            }
        }

    }

}