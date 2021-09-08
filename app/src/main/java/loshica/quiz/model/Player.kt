package loshica.quiz.model

import org.bson.Document
import org.bson.types.ObjectId

class Player {
    var _id: ObjectId
    var name: String
    var score: Int

    constructor(name: String, score: Int) {
        _id = ObjectId()
        this.name = name
        this.score = score
    }

    constructor(name: String, score: Int, _id: ObjectId) {
        this._id = _id
        this.name = name
        this.score = score
    }

    companion object {

        private const val NAME = "name"
        private const val SCORE = "score"
        private const val ID = "_id"

        fun convert(document: Document): Player {
            return Player(
                document.getString(NAME),
                document.getInteger(SCORE),
                document.getObjectId(ID)
            )
        }

        fun convert(player: Player): Document {
            return Document()
                .append(NAME, player.name)
                .append(SCORE, player.score)
                .append(ID, player._id)
        }
    }
}