package com.example.maciej.smim

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlin.properties.Delegates

class MultiplayerGameActivity : AppCompatActivity() {

    private lateinit var game: Game
    private lateinit var player: TextView
    private lateinit var playerOneScoreView: TextView
    private lateinit var playerTwoScoreView: TextView
    private lateinit var currentPlayerNumberView: TextView
    private lateinit var board: GridLayout
    private lateinit var confirmationButton: Button
    private var lastPickedField: Int = -1
    private var auth = FirebaseAuth.getInstance()
    private var db = FirebaseDatabase.getInstance()
    private var currentUser: FirebaseUser? = auth.currentUser
    private val currentUserName = currentUser?.displayName
    private lateinit var refCurrentGame : DatabaseReference
    private lateinit var username2: String
    private var playerScoreboard: Array<Int> = Array(3) {0}
    private var lostGame: Boolean = false
    private var initializingMultiplayer: Boolean = true
    private var playerNumber by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewAndDB()
        getScoreboard()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val fieldPixelSize = displayMetrics.widthPixels / board.columnCount
        val markPixelSize = fieldPixelSize/4.5f

        for ( x in 0 until board.columnCount * board.rowCount) {
            val field = Button(this)
            field.layoutParams = LinearLayout.LayoutParams(fieldPixelSize, fieldPixelSize)
            field.textSize = markPixelSize
            field.tag = x
            board.addView(field)

            field.setOnClickListener {
                // It might be best to handle all of this within the Game class but can't tell for sure
                placeMark(field, x)
            }
        }

        if(playerNumber == 1)
            freeze()
        //place mark which was added by another player
        refCurrentGame.child("move").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                refreshView()
                if(p0.child("whichPlayerTurn").exists() && p0.child("index").exists() && (p0.child("whichPlayerTurn").value as Long).toInt() == playerNumber) {
                    val button = board.getChildAt((p0.child("index").value as Long).toInt()) as Button
                    if (playerNumber == 1) {
                        button.text = game.playerMarks[1].symbol
                        button.setTextColor(game.playerMarks[1].color)
                        game.refreshFields(boardToArray())
                    } else {
                        button.text = game.playerMarks[0].symbol
                        button.setTextColor(game.playerMarks[0].color)
                        game.refreshFields(boardToArray())
                    }
                    val temp: Int = if (playerNumber == 1) 2 else 1
                    if (game.checkFields((p0.child("index").value as Long).toInt(), temp, 5)){
                        game.resetBoard()
                        resetBoardView()
                        playerScoreboard[2] += 1
                        db.getReference("users").child(currentUserName.toString()).child("loses").setValue(playerScoreboard[2])
                    }
                    else if(game.isBoardFull()){
                        println("It's a draw!")
                        game.resetBoard()
                        resetBoardView()
                        playerScoreboard[1] += 1
                        db.getReference("users").child(currentUserName.toString()).child("draws").setValue(playerScoreboard[1])
                    }
                    Toast.makeText(applicationContext, "Your turn.", Toast.LENGTH_SHORT
                    ).show()
                    unfreeze()
                }
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    private fun placeMark(field: Button, index: Int){
        val playerMark = if (playerNumber == 1) game.playerMarks[0] else game.playerMarks[1]
        if(field.text == "") {
            field.text = playerMark.symbol
            field.setTextColor(playerMark.color)
            if(lastPickedField >= 0){
                val previousField = board.getChildAt(lastPickedField) as Button
                previousField.text = ""
            }
            else{
                changeButtonState(confirmationButton)
            }
            lastPickedField = index
        }
    }

    private fun refreshView(){
        refCurrentGame.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(playerNumber == 1) {
                    playerOneScoreView.text = p0.child("score").value.toString()
                    db.getReference("users").child(username2).child("currentGame").child("score").addListenerForSingleValueEvent(object: ValueEventListener {

                        override fun onDataChange(p0: DataSnapshot) {
                            playerTwoScoreView.text = p0.value.toString()
                        }
                        override fun onCancelled(p0: DatabaseError) {}
                    })
                }
                else{
                    playerTwoScoreView.text = p0.child("score").value.toString()
                    db.getReference("users").child(username2).child("currentGame").child("score").addListenerForSingleValueEvent(object: ValueEventListener {

                        override fun onDataChange(p0: DataSnapshot) {
                            playerOneScoreView.text = p0.value.toString()
                        }
                        override fun onCancelled(p0: DatabaseError) {}
                    })
                }

                currentPlayerNumberView.text = if ((p0.child("move").child("whichPlayerTurn").value as Long).toInt() == 1) "1" else "2"
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    private fun boardToArray(): Array<Array<String>>{
        val fields = Array(board.rowCount) {Array(board.columnCount) {""} }
        for(i in 0 until board.rowCount){
            for(j in 0 until board.columnCount){
                fields[i][j] = (board.getChildAt(i*board.rowCount+j) as Button).text.toString()
            }
        }
        return fields
    }

    private fun resetBoardView(){
        for ( x in 0 until board.childCount){
            val child = board.getChildAt(x)
            (child as Button).text = ""
        }
        refCurrentGame.child("move").child("index").removeValue()
    }

    fun confirmMove(view: View){
        game.refreshFields(boardToArray())
        if(game.checkFields(lastPickedField,playerNumber,5)){
            updateScore()
            game.resetBoard()
            resetBoardView()
            playerScoreboard[0] += 1
            db.getReference("users").child(currentUserName.toString()).child("wins").setValue(playerScoreboard[0])
        }
        else if(game.isBoardFull()){
            println("It's a draw!")
            game.resetBoard()
            resetBoardView()
            playerScoreboard[1] += 1
            db.getReference("users").child(currentUserName.toString()).child("draws").setValue(playerScoreboard[1])
        }
        switchTurn()
        changeButtonState(view as Button)
        freeze()
    }

    private fun changeButtonState(button: Button){
        button.isEnabled = !button.isEnabled
        button.isClickable = !button.isClickable
    }

    private fun switchTurn(){
        refCurrentGame.child("move").child("whichPlayerTurn").addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if((p0.value as Long).toInt() == 1){
                    val move = Move()
                    move.index = lastPickedField
                    move.whichPlayerTurn = 2
                    refCurrentGame.child("move").setValue(move)
                    db.getReference("users").child(username2).child("currentGame").child("move").setValue(move)
                    game.isPlayerOneTurn = false;
                    refreshView()
                    lastPickedField = -1
                }
                else{
                    val move = Move()
                    move.index = lastPickedField
                    move.whichPlayerTurn = 1
                    refCurrentGame.child("move").setValue(move)
                    db.getReference("users").child(username2).child("currentGame").child("move").setValue(move)
                    game.isPlayerOneTurn = true
                    refreshView()
                    lastPickedField = -1
                }
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    private fun updateScore(){
        game.updateScore(playerNumber)
        refCurrentGame.child("score").setValue(game.scoreCount[playerNumber-1])
    }

    private fun initViewAndDB(){
        setContentView(R.layout.activity_multiplayer_game)

        player = findViewById(R.id.player)
        playerOneScoreView = findViewById(R.id.player_1_score)
        playerTwoScoreView = findViewById(R.id.player_2_score)
        currentPlayerNumberView = findViewById(R.id.player_number)
        confirmationButton = findViewById(R.id.confirm)
        board = findViewById(R.id.board)

        game = Game(board.rowCount,board.columnCount)
        if (currentUserName != null) {
            refCurrentGame = db.getReference("users").child(currentUserName).child("currentGame")
            refCurrentGame.removeValue()
        }
        val whichPlayerTurn: Int = if(game.isPlayerOneTurn){
            1
        } else
            2
        refCurrentGame.child("move").child("whichPlayerTurn").setValue(whichPlayerTurn)
        refCurrentGame.child("score").setValue(0)
        refreshView()
        changeButtonState(confirmationButton)
        val extras = intent.extras
        username2 = extras.get("friend") as String
        playerNumber = extras.get("playerNumber") as Int
        player.text = "Player $playerNumber"
    }

    private fun getScoreboard(){
        db.getReference("users").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                playerScoreboard[0] = (dataSnapshot.child(currentUserName.toString()).child("wins").value as Long).toInt()
                playerScoreboard[1] = (dataSnapshot.child(currentUserName.toString()).child("draws").value as Long).toInt()
                playerScoreboard[2] = (dataSnapshot.child(currentUserName.toString()).child("loses").value as Long).toInt()
            }
            override fun onCancelled(p0: DatabaseError) {}

        })
    }

    private fun freeze(){
        for(x in 0 until board.childCount){
            board.getChildAt(x).isEnabled = false
        }
    }

    private fun unfreeze(){
        for(x in 0 until board.childCount){
            board.getChildAt(x).isEnabled = true
        }
    }

    class Move{
        var whichPlayerTurn: Int = 0
        var index: Int = 0
    }
}
