package domain.utils

class CanNotCreateUserException() : RuntimeException("Can not create user")
class CanNotCreateCourseException() : RuntimeException("Can not create course")
class WordDoesNotExist() : RuntimeException("Word does not exist")
class CanNotCreateSetException() : RuntimeException("Can not create set")
class GuestLimitException() : RuntimeException("Guest limit")
