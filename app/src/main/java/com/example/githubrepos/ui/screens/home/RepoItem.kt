package com.example.githubrepos.ui.screens.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.githubrepos.R
import com.example.githubrepos.domain.model.RepoItem

@Composable
fun RepoItemScreen(
    repoItem: RepoItem,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val rotationDegree by remember(isExpanded) {
        mutableStateOf(if (isExpanded) 180f else 0f)
    }

    Card(
        modifier = modifier.animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessLow
            )
        ),
        shape = RoundedCornerShape(10.dp),
        elevation =  CardDefaults.cardElevation(63.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                model = repoItem.ownerProfilePicture,
                contentDescription = "",
                modifier = Modifier
                    .clip(RectangleShape)
                    .weight(1f),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_no_image)
            )

            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .weight(3f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = repoItem.repoName,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.basicMarquee()
                        )
                        Text(
                            text = stringResource(id = R.string.by_x, repoItem.ownerName),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray
                        )
                    }
                    IconButton(
                        onClick = { isExpanded = !isExpanded },
                        modifier = Modifier.width(IntrinsicSize.Min)
                    ) {
                        Icon(
                            Icons.Outlined.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.rotate(rotationDegree)
                        )
                    }
                }

                if (isExpanded) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = repoItem.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

