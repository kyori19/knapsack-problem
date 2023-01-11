package utils

fun Boolean.ensureSuccess() {
    if (!this) {
        error("Operation failed")
    }
}
