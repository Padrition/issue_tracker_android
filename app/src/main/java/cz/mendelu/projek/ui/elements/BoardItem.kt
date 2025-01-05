package cz.mendelu.projek.ui.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.mendelu.projek.communication.board.Board

@Composable
fun BoardItem(
    board: Board
){
    OutlinedCard (
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ){
        Text(
            text = board.name ?: "No name",
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center
        )

        Text(
            text = board.description ?: "No description",
            modifier = Modifier
                .padding(bottom = 16.dp, start = 16.dp),
            textAlign = TextAlign.Left
        )
    }
}