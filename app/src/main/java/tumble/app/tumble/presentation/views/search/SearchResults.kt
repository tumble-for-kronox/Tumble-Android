package tumble.app.tumble.presentation.views.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tumble.app.tumble.R
import tumble.app.tumble.domain.models.network.NetworkResponse

@Composable
fun SearchResults(
    searchText: String,
    numberOfSearchResults: Int,
    searchResults: List<NetworkResponse.Programme>,
    onOpenProgramme: (String) -> Unit,
    universityImage: Int?
){
    Column {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Text(
                text = stringResource(id = R.string.search_results, numberOfSearchResults),
                fontSize = 17.sp,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.weight(1f))
        }
        LazyColumn (modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            items(numberOfSearchResults){ programme ->
                ProgrammeCard(
                    programme = searchResults[programme],
                    universityImage = universityImage,
                    onOpenProgramme = onOpenProgramme)
            }
        }
    }
}