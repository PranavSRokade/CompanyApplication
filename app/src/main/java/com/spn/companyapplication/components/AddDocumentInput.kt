package com.spn.companyapplication.components

import android.content.ContentResolver
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R
import com.spn.companyapplication.viewmodels.AddLeadViewModel

@Composable
fun AddDocumentInput(
    activity: ComponentActivity,
    onDocumentPicked: (Uri?) -> Unit,
    viewModel: AddLeadViewModel,
    contentResolver: ContentResolver
) {
    val pickDocument =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { documentUri ->
                viewModel.selectedDocumentUri = documentUri
                viewModel.selectedDocumentName =
                    viewModel.getDocumentName(contentResolver, documentUri) ?: ""
                viewModel.selectedDocumentSize =
                    viewModel.getDocumentSize(contentResolver, documentUri) ?: 0L
                viewModel.selectedDocumentMimeType =
                    viewModel.getDocumentMimeType(contentResolver, documentUri) ?: ""
            }
        }
    Card(
        elevation = 0.dp,
        border = BorderStroke(
            1.dp, Color(("#9f9f9f").toColorInt())
        ),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
            .clickable {
                pickDocument.launch("*/*")
            }) {
        Column(
            Modifier
                .padding(
                    horizontal = 13.dp,
                    vertical = if (viewModel.selectedDocumentUri != null) 8.dp else 0.dp
                )
                .fillMaxSize(), verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if(viewModel.selectedDocumentUri != null) "Document" else "Upload Document", style = TextStyle(
                        fontFamily = FontFamily(Font(if(viewModel.selectedDocumentUri != null) R.font.outfit_medium else R.font.outfit_regular)),
                        fontSize = 17.sp,
                        color = Color.Black
                    )
                )

                if(viewModel.selectedDocumentUri == null){
                    Icon(
                        painter = painterResource(id = R.drawable.ic_upload),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }


            if (viewModel.selectedDocumentUri != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    viewModel.selectedDocumentName, style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.outfit_regular)),
                        fontSize = 17.sp,
                        color = Color.Black
                    )
                )
                Text(
                    viewModel.selectedDocumentMimeType, style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.outfit_regular)),
                        fontSize = 17.sp,
                        color = Color.Black
                    )
                )
                Text(
                    viewModel.selectedDocumentSize.toString(), style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.outfit_regular)),
                        fontSize = 17.sp,
                        color = Color.Black
                    )
                )
            }
        }
    }
}